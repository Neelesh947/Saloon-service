package com.keycloak.utils;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.logging.log4j.util.InternalException;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Component;

import com.exception.handling.models.ValidationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.keycloak.dto.ErrorResponseDto;
import com.keycloak.dto.KeycloakProperties;
import com.keycloak.dto.LoginDto;
import com.keycloak.dto.RoleRepresentationDTO;
import com.keycloak.dto.TokenResponseDto;
import com.keycloak.dto.UserCredentialDTO;
import com.keycloak.function.QuadConsumer;
import com.keycloak.function.TriConsumer;
import com.keycloak.function.TriFunction;
import com.fasterxml.jackson.annotation.JsonInclude;

@Component
public class KeycloakHandler {

	private HttpClient httpClient;

	private KeycloakProperties keycloakProperties;

	public KeycloakHandler(HttpClient client, KeycloakProperties properties) {
		this.httpClient = client;
		this.keycloakProperties = properties;
	}

	public final BiFunction<String, List<NameValuePair>, TokenResponseDto> accesstoken = (url, requestBody) -> {
		ObjectMapper objectMapper = new ObjectMapper();
		TokenResponseDto responseDto;
		try {
			String formBody = buildFormBody(requestBody);
			HttpRequest request = HttpRequest.newBuilder().uri(new URI(url))
					.header(Constants.CONTENT_TYPE, Constants.X_WWW_FORM_URLENCODED)
					.POST(HttpRequest.BodyPublishers.ofString(formBody)).build();
			HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
			if (response.statusCode() >= 400 && response.statusCode() < 600) {
				ErrorResponseDto errorResponse = objectMapper.readValue(response.body(), ErrorResponseDto.class);
				throw new ValidationException(errorResponse.getErrorDescription());
			}
			responseDto = objectMapper.readValue(response.body(), TokenResponseDto.class);
		} catch (ValidationException e) {
			throw e;
		} catch (Exception e) {
			throw new InternalException(e);
		}
		return responseDto;
	};

	private static String buildFormBody(List<NameValuePair> params) {
		return params.stream().map(p -> URLEncoder.encode(p.getName(), StandardCharsets.UTF_8) + "="
				+ URLEncoder.encode(p.getValue(), StandardCharsets.UTF_8)).collect(Collectors.joining("&"));
	}

	private final Supplier<TokenResponseDto> accessTokenAdminCli = () -> {
		String url = MessageFormat.format(keycloakProperties.getTokenUrl(), Constants.MASTER_REALM);
		List<NameValuePair> body = Stream.<NameValuePair>of(
				new BasicNameValuePair(Constants.GRANT_TYPE, Constants.PASSWORD),
				new BasicNameValuePair(Constants.CLIENT_ID, keycloakProperties.getAdminClient()),
				new BasicNameValuePair(Constants.USERNAME, keycloakProperties.getAdminCredentials().getUsername()),
				new BasicNameValuePair(Constants.PASSWORD, keycloakProperties.getAdminCredentials().getPassword()))
				.toList();
		return accesstoken.apply(url, body);
	};

	public final BiFunction<LoginDto, String, TokenResponseDto> userAccessToken = (credentials, realm) -> {
		String url = MessageFormat.format(keycloakProperties.getTokenUrl(), realm);
		List<NameValuePair> body = Stream
				.<NameValuePair>of(new BasicNameValuePair(Constants.GRANT_TYPE, Constants.PASSWORD),
						new BasicNameValuePair(Constants.CLIENT_ID, keycloakProperties.getResource()),
						new BasicNameValuePair(Constants.USERNAME, credentials.getUsername()),
						new BasicNameValuePair(Constants.PASSWORD, credentials.getPassword()), new BasicNameValuePair(
								Constants.CLIENT_SECRET, keycloakProperties.getCredentials().getSecrets().get(realm)))
				.toList();
		return accesstoken.apply(url, body);
	};

	public final BiFunction<Object[], Void, Map<String, String>> createUserFn = (args, v) -> {
		Object userObject = args[0];
		String role = (String) args[1];
		String realm = (String) args[2];
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			String adminToken = accessTokenAdminCli.get().getAccessToken();
			String url = MessageFormat.format(keycloakProperties.getCreateUserUrl(), realm, role);
			String requestBody = objectMapper.writeValueAsString(userObject);
			HttpRequest request = HttpRequest.newBuilder().uri(new URI(url))
					.header(Constants.CONTENT_TYPE, Constants.APPLICATION_JSON)
					.header("Authorization", "Bearer " + adminToken)
					.POST(HttpRequest.BodyPublishers.ofString(requestBody)).build();
			HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

			if (response.statusCode() >= 400 && response.statusCode() < 600) {
				ErrorResponseDto error = objectMapper.readValue(response.body(), ErrorResponseDto.class);
				throw new ValidationException(error.getErrorDescription());
			}

			return Map.of("status", "success", "message", "User Created");
		} catch (ValidationException e) {
			throw e;
		} catch (Exception e) {
			throw new InternalException(e);
		}
	};

	public final BiConsumer<String, List<NameValuePair>> logout = (url, body) -> {
		try {
			String formBody = buildFormBody(body);
			HttpRequest request = HttpRequest.newBuilder().uri(new URI(url))
					.header(Constants.CONTENT_TYPE, Constants.X_WWW_FORM_URLENCODED)
					.POST(HttpRequest.BodyPublishers.ofString(formBody)).build();
			HttpResponse<Void> response = httpClient.send(request, HttpResponse.BodyHandlers.discarding());
			int statusCode = response.statusCode();
			if (statusCode < 200 || statusCode >= 300) {
				throw new InternalException("Logout failed with status code: " + statusCode);
			}
		} catch (Exception e) {
			throw new InternalException(e);
		}
	};

	private final Function<String, JSONObject> createPasswordJson = password -> {
		try {
			JSONObject passwordJson = new JSONObject();
			passwordJson.put(Constants.TYPE, Constants.PASSWORD);
			passwordJson.put(Constants.VALUE, password);
			passwordJson.put(Constants.TEMPORARY, false);
			return passwordJson;
		} catch (JSONException e) {
			throw new InternalException(e);
		}
	};

	public final TriConsumer<UserCredentialDTO, String, String> generateResetPassword = (userCredential, token,
			userId) -> {
		String realm = userCredential.getRealm();
		String resetUrl = MessageFormat.format(keycloakProperties.getResetPasswordUrl(), realm, userId);
		JSONObject passwordPayload = createPasswordJson.apply(userCredential.getPassword());
		try {
			HttpRequest request = HttpRequest.newBuilder().uri(new URI(resetUrl))
					.header(Constants.CONTENT_TYPE, Constants.APPLICATION_JSON)
					.header(Constants.AUTHORIZATION, Constants.BEARER + token)
					.PUT(HttpRequest.BodyPublishers.ofString(passwordPayload.toString(), StandardCharsets.UTF_8))
					.build();
			HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
			int statusCode = response.statusCode();
			if (statusCode >= 400 && statusCode < 600) {
				ObjectMapper objectMapper = new ObjectMapper();
				String responseString = response.body();
				ErrorResponseDto errorResponse = objectMapper.readValue(responseString, ErrorResponseDto.class);
				throw new ValidationException(errorResponse.getErrorDescription());
			}
		} catch (ValidationException ex) {
			throw ex;
		} catch (Exception e) {
			throw new InternalException(e);
		}
	};

	public final TriConsumer<UserCredentialDTO, String, String> forgotPassword = (userCredential, token, userId) -> {
		String lifespan = String.valueOf(keycloakProperties.getUpdatePasswordLifespan()).replace(",", "");
		String forgotPasswordUrl = MessageFormat.format(keycloakProperties.getForgotPasswordUrl(), userId,
				keycloakProperties.getResource(), lifespan, keycloakProperties.getUpdatePasswordRedirectUri());
		try {
			String jsonBody = new ObjectMapper().writeValueAsString(List.of("UPDATE_PASSWORD"));
			HttpRequest request = HttpRequest.newBuilder().uri(new URI(forgotPasswordUrl))
					.header(Constants.CONTENT_TYPE, Constants.APPLICATION_JSON)
					.header(Constants.AUTHORIZATION, Constants.BEARER + token)
					.PUT(HttpRequest.BodyPublishers.ofString(jsonBody, StandardCharsets.UTF_8)).build();
			HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
			int statusCode = response.statusCode();
			if (statusCode >= 400 && statusCode < 600) {
				ObjectMapper objectMapper = new ObjectMapper();
				String responseString = response.body();
				ErrorResponseDto errorResponse = objectMapper.readValue(responseString, ErrorResponseDto.class);
				throw new ValidationException(errorResponse.getErrorMessage());
			}
		} catch (ValidationException ex) {
			throw ex;
		} catch (JsonProcessingException e) {
			throw new InternalException(e);
		} catch (Exception e) {
			throw new InternalException(e);
		}
	};

	public final BiFunction<String, UserRepresentation, String> createKeycloakUser = (realm, userRepresentation) -> {
		String userId = null;
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			String url = MessageFormat.format(keycloakProperties.getAllUsers(), realm);
			objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
			String json = objectMapper.writeValueAsString(userRepresentation);
			HttpRequest request = HttpRequest.newBuilder().uri(new URI(url))
					.header(Constants.AUTHORIZATION, Constants.BEARER + accessTokenAdminCli.get())
					.header(Constants.CONTENT_TYPE, Constants.APPLICATION_JSON)
					.POST(HttpRequest.BodyPublishers.ofString(json, StandardCharsets.UTF_8)).build();
			HttpResponse<String> keycloakResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
			int statusCode = keycloakResponse.statusCode();
			if (statusCode >= 400 && statusCode < 600) {
				String responseString = keycloakResponse.body();
				ErrorResponseDto errorResponse = objectMapper.readValue(responseString, ErrorResponseDto.class);
				throw new ValidationException(errorResponse.getErrorMessage());
			}
			HttpHeaders headers = keycloakResponse.headers();
			List<String> locationHeader = headers.map().get(Constants.LOCATION_HEADER);
			if (locationHeader != null && !locationHeader.isEmpty()) {
				String locationUrl = locationHeader.get(0);
				String regex = Constants.USER_ID_REGEX;
				Pattern pattern = Pattern.compile(regex);
				Matcher matcher = pattern.matcher(locationUrl);
				if (matcher.find()) {
					userId = matcher.group(1);
				}
			}
		} catch (ValidationException ex) {
			throw ex;
		} catch (Exception e) {
			throw new InternalException(e);
		}
		return userId;
	};

	public final BiFunction<String, String, List<RoleRepresentationDTO>> keycloakRoles = (role, realm) -> {
		String url = MessageFormat.format(keycloakProperties.getRoleDetails(), realm, role);
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			HttpRequest getRequest = HttpRequest.newBuilder().uri(new URI(url))
					.header(Constants.AUTHORIZATION, Constants.BEARER + accessTokenAdminCli.get())
					.header(Constants.CONTENT_TYPE, Constants.APPLICATION_JSON).GET().build();
			HttpResponse<String> response = httpClient.send(getRequest, HttpResponse.BodyHandlers.ofString());
			int statusCode = response.statusCode();
			if (statusCode >= 400 && statusCode < 600) {
				String responseString = response.body();
				ErrorResponseDto errorResponse = objectMapper.readValue(responseString, ErrorResponseDto.class);
				throw new ValidationException(errorResponse.getError());
			}
			String responseString = response.body();
			return Collections.singletonList(objectMapper.readValue(responseString, RoleRepresentationDTO.class));
		} catch (ValidationException ex) {
			throw ex;
		} catch (Exception e) {
			throw new InternalException(e);
		}
	};

	public final TriConsumer<List<RoleRepresentationDTO>, String, String> assignRoleToUser = (roles, realm, userId) -> {
		String url = MessageFormat.format(keycloakProperties.getUserRoleMappings(), realm, userId);
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			String jsonRoles = objectMapper.writeValueAsString(roles);
			HttpRequest postRequest = HttpRequest.newBuilder().uri(new URI(url))
					.header(Constants.AUTHORIZATION, Constants.BEARER + accessTokenAdminCli.get())
					.header(Constants.CONTENT_TYPE, Constants.APPLICATION_JSON)
					.POST(HttpRequest.BodyPublishers.ofString(jsonRoles)).build();
			HttpResponse<String> response = httpClient.send(postRequest, HttpResponse.BodyHandlers.ofString());
			int statusCode = response.statusCode();
			if (statusCode >= 400 && statusCode < 600) {
				String responseString = response.body();
				ErrorResponseDto errorResponse = objectMapper.readValue(responseString, ErrorResponseDto.class);
				throw new ValidationException(errorResponse.getErrorMessage());
			}
		} catch (ValidationException ex) {
			throw ex;
		} catch (Exception e) {
			throw new InternalException(e);
		}
	};

	public TriConsumer<UserRepresentation, String, String> updateKeycloakUser = (userRepresentation, userId, realm) -> {
		String url = MessageFormat.format(keycloakProperties.getUserById(), realm, userId);
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		try {
			String requestJson = objectMapper.writeValueAsString(userRepresentation);
			HttpRequest putRequest = HttpRequest.newBuilder().uri(new URI(url))
					.header(Constants.AUTHORIZATION, Constants.BEARER + accessTokenAdminCli.get())
					.header(Constants.CONTENT_TYPE, Constants.APPLICATION_JSON)
					.PUT(HttpRequest.BodyPublishers.ofString(requestJson)).build();
			HttpResponse<String> response = httpClient.send(putRequest, HttpResponse.BodyHandlers.ofString());
			int statusCode = response.statusCode();
			if (statusCode >= 400 && statusCode < 600) {
				String responseString = response.body();
				ErrorResponseDto errorResponse = objectMapper.readValue(responseString, ErrorResponseDto.class);
				throw new ValidationException(errorResponse.getErrorMessage());
			}
		} catch (ValidationException ex) {
			throw ex;
		} catch (Exception e) {
			throw new InternalException(e);
		}
	};

	public final BiConsumer<String, String> deleteKeycloakUser = (userId, realm) -> {
		String url = MessageFormat.format(keycloakProperties.getUserById(), realm, userId);
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			HttpRequest deleteRequest = HttpRequest.newBuilder().uri(new URI(url))
					.header(Constants.AUTHORIZATION, Constants.BEARER + accessTokenAdminCli.get())
					.header(Constants.CONTENT_TYPE, Constants.APPLICATION_JSON).DELETE().build();
			HttpResponse<String> response = httpClient.send(deleteRequest, HttpResponse.BodyHandlers.ofString());
			int statusCode = response.statusCode();
			if (statusCode >= 400 && statusCode < 600) {
				String responseString = response.body();
				ErrorResponseDto errorResponse = objectMapper.readValue(responseString, ErrorResponseDto.class);
				throw new ValidationException(errorResponse.getErrorMessage());
			}
		} catch (ValidationException ex) {
			throw ex;
		} catch (Exception e) {
			throw new InternalException(e);
		}
	};

	public final Function<String, List<UserRepresentation>> userDataDetails = url -> {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			HttpRequest getRequest = HttpRequest.newBuilder().uri(new URI(url))
					.header(Constants.AUTHORIZATION, Constants.BEARER + accessTokenAdminCli.get())
					.header(Constants.CONTENT_TYPE, Constants.APPLICATION_JSON).GET().build();
			HttpResponse<String> response = httpClient.send(getRequest, HttpResponse.BodyHandlers.ofString());
			int statusCode = response.statusCode();
			String responseString = response.body();
			if (statusCode >= 400 && statusCode < 600) {
				ErrorResponseDto errorResponse = objectMapper.readValue(responseString, ErrorResponseDto.class);
				throw new ValidationException(errorResponse.getError());
			}
			JsonNode jsonNode = objectMapper.readTree(responseString);
			if (jsonNode.isArray()) {
				return objectMapper.readValue(responseString, new TypeReference<List<UserRepresentation>>() {
				});
			} else {
				UserRepresentation singleObject = objectMapper.readValue(responseString, UserRepresentation.class);
				return Collections.singletonList(singleObject);
			}
		} catch (ValidationException ex) {
			throw ex;
		} catch (Exception e) {
			throw new InternalException(e);
		}
	};

	public final TriFunction<String, String, String, String> clientToken = (realm, realmClientId, clientSecret) -> {
		String url = MessageFormat.format(keycloakProperties.getTokenUrl(), realm);
		List<NameValuePair> body = Stream
				.<NameValuePair>of(new BasicNameValuePair(Constants.GRANT_TYPE, Constants.CLIENT_CREDENTIALS),
						new BasicNameValuePair(Constants.CLIENT_ID, realmClientId),
						new BasicNameValuePair(Constants.CLIENT_SECRET, clientSecret))
				.toList();
		return accesstoken.apply(url, body).getAccessToken();
	};

	public final BinaryOperator<String> keycloakUserId = (url, token) -> {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			HttpRequest request = HttpRequest.newBuilder().uri(new URI(url))
					.header(Constants.CONTENT_TYPE, Constants.APPLICATION_JSON)
					.header(Constants.AUTHORIZATION, Constants.BEARER + token).GET().build();
			HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
			JsonNode userObject = objectMapper.readTree(response.body());
			if (userObject.isEmpty() || !userObject.has(0)) {
				throw new ValidationException("Invalid Username");
			}
			Optional.ofNullable(userObject.get(0).get(Constants.ENABLED)).filter(JsonNode::asBoolean)
					.orElseThrow(() -> new ValidationException("User is dissabled"));

			String userId = userObject.get(0).get(Constants.ID).asText();
			return userId;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	};

	public final QuadConsumer<String, String, String, String> clearOldSession = (userId, realm, token, url) -> {
		ObjectMapper objectMapper = new ObjectMapper();
		String requestUrl = MessageFormat.format(url, realm, userId);
		try {
			HttpRequest request = HttpRequest.newBuilder().uri(new URI(requestUrl))
					.header(Constants.AUTHORIZATION, Constants.BEARER + token)
					.header(Constants.CONTENT_TYPE, Constants.APPLICATION_JSON)
					.POST(HttpRequest.BodyPublishers.ofString("")).build();
			HttpResponse<String> keycloakResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
			int statusCode = keycloakResponse.statusCode();
			if (statusCode >= 400 && statusCode < 600) {
				ErrorResponseDto errorResponse = objectMapper.readValue(keycloakResponse.body(),
						ErrorResponseDto.class);
				throw new ValidationException(errorResponse.getErrorDescription());
			}
		} catch (ValidationException e) {
			throw e;
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	};
}
