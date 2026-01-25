package com.keycloak.service;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.logging.log4j.util.InternalException;
import org.keycloak.representations.idm.EventRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.exception.handling.models.NotFoundException;
import com.exception.handling.models.ValidationException;
import com.keycloak.dto.ClientCredentialsDTO;
import com.keycloak.dto.KeycloakProperties;
import com.keycloak.dto.KeycloakUserDto;
import com.keycloak.dto.RoleRepresentationDTO;
import com.keycloak.dto.TokenResponseDto;
import com.keycloak.dto.UserCredentialDTO;
import com.keycloak.function.QuadFunction;
import com.keycloak.function.TriConsumer;
import com.keycloak.function.TriFunction;
import com.keycloak.utils.Constants;
import com.keycloak.utils.KeycloakHandler;
import com.keycloak.utils.KeycloakObjectMapper;

@Service
public class KeycloakService {

	private KeycloakHandler keycloakHandler;
	private KeycloakProperties keycloakProperties;
	private KeycloakObjectMapper keycloakObjectMapper;

	public KeycloakService(KeycloakHandler keycloakHandler, KeycloakProperties keycloakProperties,
			KeycloakObjectMapper keycloakObjectMapper) {
		this.keycloakHandler = keycloakHandler;
		this.keycloakProperties = keycloakProperties;
		this.keycloakObjectMapper = keycloakObjectMapper;
	}

	private final Function<UserCredentialDTO, String> loginURL = userCredential -> {
		String url = keycloakProperties.getUserNameUrl();
		return MessageFormat.format(url, userCredential.getRealm(), userCredential.getUserName());
	};

	/**
	 * Dashboard client secrete
	 */
	private final UnaryOperator<String> dashboardClientSecret = realm -> {
		return Optional.ofNullable(keycloakProperties.getCredentials().getSecrets().get(realm))
				.orElseThrow(() -> new ValidationException("Invalid Realm"));
	};

	/**
	 * A function that generates a list of name-value pairs to construct the body of
	 * a login request. The function takes a {@code UserCredentialDTO} object as
	 * input, extracts the necessary information such as username and password, and
	 * combines it with additional constants (e.g., grant type, client ID, client
	 * secret) obtained from the application's external configuration and helper
	 * methods. The resulting list of name-value pairs can be used as parameters for
	 * making a login request to the authentication server. Logging is performed to
	 * track the username used in the login request. This function ensures that the
	 * required parameters are correctly structured for authentication requests.
	 */
	private final Function<UserCredentialDTO, List<NameValuePair>> loginRequestBody = userCredential -> {
		String clientSecret = dashboardClientSecret.apply(userCredential.getRealm());
		return Stream
				.of(new BasicNameValuePair(Constants.GRANT_TYPE, Constants.GRANT_TYPE_PASSWORD),
						new BasicNameValuePair(Constants.CLIENT_ID, keycloakProperties.getResource()),
						new BasicNameValuePair(Constants.CLIENT_SECRET, clientSecret),
						new BasicNameValuePair(Constants.USERNAME, userCredential.getUserName()),
						new BasicNameValuePair(Constants.PASSWORD, userCredential.getPassword()))
				.map(pair -> (NameValuePair) pair).toList();
	};

	/**
	 * Direct grant
	 */
	private final Function<UserCredentialDTO, TokenResponseDto> directToken = userCredential -> {
		String url = MessageFormat.format(keycloakProperties.getTokenUrl(), userCredential.getRealm());
		return keycloakHandler.accesstoken.apply(url, loginRequestBody.apply(userCredential));
	};

	/**
	 * Regular login username password
	 */
	private final BiFunction<UserCredentialDTO, String, TokenResponseDto> standardLogin = (userCredentials, url) -> {
		String realm = userCredentials.getRealm();
		String dashboardSecret = dashboardClientSecret.apply(realm);
		String dashboardClientId = keycloakProperties.getResource();
		String clientToken = keycloakHandler.clientToken.apply(realm, dashboardClientId, dashboardSecret);
		String userId = keycloakHandler.keycloakUserId.apply(url, clientToken);
		keycloakHandler.clearOldSession.accept(userId, realm, clientToken, keycloakProperties.getClearSessionUrl());
		return directToken.apply(userCredentials);
	};

	/**
	 * Login
	 */
	public final Function<UserCredentialDTO, TokenResponseDto> loginUser = userCredential -> {
		String url = loginURL.apply(userCredential);
		TokenResponseDto response = standardLogin.apply(userCredential, url);
		return response;
	};

	/**
	 * based on the type of user get client token
	 */
	private final Function<String, ClientCredentialsDTO> determineClientCredentials = (realm) -> {
		String clientId = keycloakProperties.getResource();
		String clientSecret = dashboardClientSecret.apply(realm);
		return new ClientCredentialsDTO(clientId, clientSecret);
	};

	/**
	 * refresh token request body
	 */
	private final TriFunction<String, String, String, List<NameValuePair>> refreshTokenBody = (clientId, clientSecret,
			refreshToken) -> {
		return Stream
				.of(new BasicNameValuePair(Constants.GRANT_TYPE, Constants.REFRESH_TOKEN),
						new BasicNameValuePair(Constants.CLIENT_ID, clientId),
						new BasicNameValuePair(Constants.CLIENT_SECRET, clientSecret),
						new BasicNameValuePair(Constants.REFRESH_TOKEN, refreshToken))
				.map(pair -> (NameValuePair) pair).toList();
	};

	/**
	 * Refresh access token
	 */
	public final BiFunction<String, String, TokenResponseDto> refreshAccessToken = (refreshToken, realm) -> {
		ClientCredentialsDTO credentials = determineClientCredentials.apply(realm);
		String url = MessageFormat.format(keycloakProperties.getTokenUrl(), realm);
		return keycloakHandler.accesstoken.apply(url,
				refreshTokenBody.apply(credentials.getClientId(), credentials.getClientSecret(), refreshToken));
	};

	/**
	 * logout request body
	 */
	private final TriFunction<String, String, String, List<NameValuePair>> logoutBody = (clientId, clientSecret,
			refreshToken) -> {
		return Stream
				.of(new BasicNameValuePair(Constants.CLIENT_ID, clientId),
						new BasicNameValuePair(Constants.CLIENT_SECRET, clientSecret),
						new BasicNameValuePair(Constants.REFRESH_TOKEN, refreshToken))
				.map(pair -> (NameValuePair) pair).toList();
	};

	/**
	 * logout
	 */
	public final BiConsumer<String, String> logout = (refreshToken, realm) -> {
		ClientCredentialsDTO credentials = determineClientCredentials.apply(realm);
		String url = MessageFormat.format(keycloakProperties.getLogoutUrl(), realm);
		keycloakHandler.logout.accept(url,
				logoutBody.apply(credentials.getClientId(), credentials.getClientSecret(), refreshToken));
	};

	public Map<String, String> createUser(Object userObject, String role, String realm) {
		return keycloakHandler.createUserFn.apply(new Object[] { userObject, role, realm }, null);
	}

	/**
	 * Password operation like reset and forgot password handler
	 */
	private final BiConsumer<UserCredentialDTO, TriConsumer<UserCredentialDTO, String, String>> passwordOperationHandler = (
			userCredential, passwordOperation) -> {
		String realm = userCredential.getRealm();
		String userName = userCredential.getUserName();
		String clientSecret = dashboardClientSecret.apply(realm);
		String clientToken = keycloakHandler.clientToken.apply(realm, keycloakProperties.getResource(), clientSecret);
		String url = MessageFormat.format(keycloakProperties.getUserNameUrl(), realm, userName);
		String userId = keycloakHandler.keycloakUserId.apply(url, clientToken);
		passwordOperation.accept(userCredential, clientToken, userId);
	};

	/**
	 * reset password
	 */
	public final Consumer<UserCredentialDTO> resetPassword = userCredential -> passwordOperationHandler
			.accept(userCredential, (uc, clientToken, userId) -> keycloakHandler.generateResetPassword
					.accept(userCredential, clientToken, userId));

	/**
	 * Forgot password
	 */
	public final Consumer<UserCredentialDTO> forgotPassword = userCredential -> passwordOperationHandler.accept(
			userCredential,
			(uc, clientToken, userId) -> keycloakHandler.forgotPassword.accept(userCredential, clientToken, userId));

	/**
	 * create user
	 */
	public final TriFunction<KeycloakUserDto, String, String, String> createUser = (userDTO, role, realm) -> {
		UserRepresentation userRepresentation = keycloakObjectMapper.keycloakUserRepresentation.apply(userDTO, role);
		String userId = keycloakHandler.createKeycloakUser.apply(realm, userRepresentation);
		List<RoleRepresentationDTO> rolesInRealm = keycloakHandler.keycloakRoles.apply(role, realm);
		keycloakHandler.assignRoleToUser.accept(rolesInRealm, realm, userId);
		return userId;
	};

	/**
	 * update user
	 */
	public final TriConsumer<UserRepresentation, String, String> updateUser = (userDTO, userId,
			realm) -> keycloakHandler.updateKeycloakUser.accept(userDTO, userId, realm);

	/**
	 * Delete user
	 */
	public final BiConsumer<String, String> deleteUser = (userId, realm) -> keycloakHandler.deleteKeycloakUser
			.accept(userId, realm);

	/**
	 * get user by user id
	 */
	public final BiFunction<String, String, UserRepresentation> userById = (userId, realm) -> {
		String url = MessageFormat.format(keycloakProperties.getUserById(), realm, userId);
		return keycloakHandler.userDataDetails.apply(url).stream().findFirst()
				.orElseThrow(() -> new NotFoundException("Invalid User Id"));
	};

	/**
	 * user by role and name
	 */
	public final TriFunction<String, String, String, List<UserRepresentation>> userByNameAndRole = (userName, role,
			realm) -> {
		String url = !ObjectUtils.isEmpty(role)
				? MessageFormat.format(keycloakProperties.getUserByRoleUsername(), realm, userName, role)
				: MessageFormat.format(keycloakProperties.getUserByUsername(), realm, userName);
		return keycloakHandler.userDataDetails.apply(url);
	};

	/**
	 * user by email and role
	 */
	public final TriFunction<String, String, String, List<UserRepresentation>> userByEmailAndRole = (email, role,
			realm) -> {
		String url = MessageFormat.format(keycloakProperties.getUserByEmail(), realm, email, role);
		return keycloakHandler.userDataDetails.apply(url);
	};

	/**
	 * user by role and phone number
	 */
	public final TriFunction<String, String, String, List<UserRepresentation>> userByPhoneAndRole = (phone, role,
			realm) -> {
		String url = MessageFormat.format(keycloakProperties.getUserByPhone(), realm, phone);
		return keycloakHandler.userDataDetails.apply(url).stream().filter(user -> user.getAttributes() != null
				&& user.getAttributes().get("roles") != null && user.getAttributes().get("roles").contains(role))
				.toList();
	};

	/**
	 * get all users
	 */
	public final Function<String, List<UserRepresentation>> allUserByRole = role -> keycloakProperties.getRealms()
			.stream().flatMap(realm -> {
				String maxInt = String.valueOf(Integer.MAX_VALUE).replace(",", "");
				String url = MessageFormat.format(keycloakProperties.getUserByRole(), realm, role, maxInt);
				try {
					return keycloakHandler.userDataDetails.apply(url).stream();
				} catch (Exception ex) {
					throw new InternalException(ex);
				}
			}).toList();
	/**
	 * search user based on search criteria
	 */
	public final TriFunction<String, String, String, List<UserRepresentation>> searchUser = (request, role, realm) -> {
		String maxInt = String.valueOf(Integer.MAX_VALUE).replace(",", "");
		String url = MessageFormat.format(keycloakProperties.getUserByRole(), realm, role, maxInt);
		List<UserRepresentation> users = keycloakHandler.userDataDetails.apply(url);
		if (request != null) {
			String[] searchPair = request.split(":");
			if (searchPair.length < 2) {
				throw new ValidationException("Invalid search string");
			}
			String searchKey = searchPair[0].toLowerCase();
			String searchValue = searchPair[1].toLowerCase();
			Map<String, Predicate<UserRepresentation>> searchOperations = Map.of(Constants.USERNAME,
					ur -> ur.getUsername() != null && ur.getUsername().toLowerCase().contains(searchValue),
					Constants.EMAIL, ur -> ur.getEmail() != null && ur.getEmail().toLowerCase().contains(searchValue),
					Constants.FIRSTNAME,
					ur -> ur.getFirstName() != null && ur.getFirstName().toLowerCase().contains(searchValue),
					Constants.LASTNAME,
					ur -> ur.getLastName() != null && ur.getLastName().toLowerCase().contains(searchValue),
					Constants.PHONENUMBER,
					ur -> Optional.ofNullable(ur.getAttributes().get(Constants.PHONENUMBER))
							.flatMap(phoneNumbers -> phoneNumbers.stream().findFirst())
							.map(phone -> phone.contains(searchValue)).orElse(false),
					Constants.ENABLED,
					ur -> ur.isEnabled() != null && ur.isEnabled().toString().equalsIgnoreCase(searchValue),
					Constants.COMPANYNAME,
					ur -> Optional.ofNullable(ur.getAttributes().get(Constants.COMPANYNAME))
							.flatMap(companyNames -> companyNames.stream().findFirst())
							.map(company -> company.toLowerCase().contains(searchValue)).orElse(false),
					Constants.ASSIGNED_SENDERS,
					ur -> Optional.ofNullable(ur.getAttributes().get(Constants.ASSIGNED_SENDERS))
							.flatMap(assignedSenders -> assignedSenders.stream().findFirst())
							.map(sender -> sender.toLowerCase().contains(searchValue)).orElse(false));
			Predicate<UserRepresentation> defaultPredicate = ur -> false;
			return users.stream().filter(searchOperations.getOrDefault(searchKey, defaultPredicate)).toList();
		}
		return users;
	};

	/**
	 * Get login and logout events for a user with pagination.
	 */
	public final QuadFunction<String, String, Integer, Integer, List<EventRepresentation>> getLoginLogoutEvents = (
			userId, realm, page, size) -> {
		int first = page * size;
		String url = MessageFormat.format(keycloakProperties.getEventUrl(), realm, userId, first, size);
		List<EventRepresentation> events = keycloakHandler.getLoginLogoutEvents.apply(url);
		return events;
	};
}
