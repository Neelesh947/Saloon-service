package com.keycloak.utils;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Stream;

import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.apache.hc.core5.net.URLEncodedUtils;
import org.apache.logging.log4j.util.InternalException;
import org.springframework.stereotype.Component;

import com.exception.handling.models.ValidationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.keycloak.dto.ErrorResponseDto;
import com.keycloak.dto.KeycloakProperties;
import com.keycloak.dto.LoginDto;
import com.keycloak.dto.TokenResponseDto;

@Component
public class KeycloakHandler {

	private HttpClient httpClient;

	private KeycloakProperties keycloakProperties;

	private KeycloakHandler(HttpClient client, KeycloakProperties properties) {
		this.httpClient = client;
		this.keycloakProperties = properties;
	}

	@SuppressWarnings("deprecation")
	public final BiFunction<String, List<NameValuePair>, TokenResponseDto> accesstoken = (url, requestBody) -> {
		ObjectMapper objectMapper = new ObjectMapper();
		TokenResponseDto responseDto;
		try {
			String formBody = URLEncodedUtils.format(requestBody, StandardCharsets.UTF_8);
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

//	private final Supplier<TokenResponseDto> accessTokenAdminCli = () -> {
//		String url = MessageFormat.format(keycloakProperties.getTokenUrl(), Constants.MASTER_REALM);
//		List<NameValuePair> body = Stream.<NameValuePair>of(
//				new BasicNameValuePair(Constants.GRANT_TYPE, Constants.PASSWORD),
//				new BasicNameValuePair(Constants.CLIENT_ID, keycloakProperties.getAdminClient()),
//				new BasicNameValuePair(Constants.USERNAME, keycloakProperties.getAdminCredentials().getUsername()),
//				new BasicNameValuePair(Constants.PASSWORD, keycloakProperties.getAdminCredentials().getPassword()))
//				.toList();
//		return accesstoken.apply(url, body);
//	};

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
}
