package com.keycloak.utils;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.logging.log4j.util.InternalException;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Component;

import com.exception.handling.models.ValidationException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.keycloak.dto.ErrorResponseDto;
import com.keycloak.dto.KeycloakProperties;
import com.keycloak.dto.LoginDto;
import com.keycloak.dto.TokenResponseDto;
import com.keycloak.function.QuadConsumer;
import com.keycloak.function.TriFunction;

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

	public final BiFunction<Object[], Void, Void> updateUserFn = (args, v) -> {
		UserRepresentation user = (UserRepresentation) args[0];
		String userId = (String) args[1];
		String realm = (String) args[2];

		ObjectMapper mapper = new ObjectMapper();
		try {
			String adminToken = accessTokenAdminCli.get().getAccessToken();
			String url = MessageFormat.format(keycloakProperties.getUpdateUserUrl(), realm, userId);

			String body = mapper.writeValueAsString(user);

			HttpRequest request = HttpRequest.newBuilder().uri(new URI(url))
					.header(Constants.CONTENT_TYPE, Constants.APPLICATION_JSON)
					.header("Authorization", "Bearer " + adminToken).PUT(HttpRequest.BodyPublishers.ofString(body))
					.build();

			httpClient.send(request, HttpResponse.BodyHandlers.discarding());
			return null;
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
