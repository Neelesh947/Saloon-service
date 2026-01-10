package com.keycloak.service;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import com.exception.handling.models.ValidationException;
import com.keycloak.dto.ClientCredentialsDTO;
import com.keycloak.dto.KeycloakProperties;
import com.keycloak.dto.TokenResponseDto;
import com.keycloak.dto.UserCredentialDTO;
import com.keycloak.function.TriFunction;
import com.keycloak.utils.Constants;
import com.keycloak.utils.KeycloakHandler;

@Service
public class KeycloakService {

	private KeycloakHandler keycloakHandler;
	private KeycloakProperties keycloakProperties;

	public KeycloakService(KeycloakHandler keycloakHandler, KeycloakProperties keycloakProperties) {
		this.keycloakHandler = keycloakHandler;
		this.keycloakProperties = keycloakProperties;
	}

	private final Function<UserCredentialDTO, String> loginURL = userCredential -> {
		String url = keycloakProperties.getTokenUrl();
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
	 * Regular login username password or one code/QR
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
		TokenResponseDto response;
		response = standardLogin.apply(userCredential, url);
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

	public void updateUser(UserRepresentation userObject, String userId, String realm) {
		keycloakHandler.updateUserFn.apply(new Object[] { userObject, userId, realm }, null);
	}

}
