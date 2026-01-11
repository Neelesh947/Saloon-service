package com.keycloak.dto;

import java.util.List;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "keycloak")
public class KeycloakProperties {

	private String resource;

	private List<String> realms;

	private String tokenUrl;

	private String adminClient;

	private AdminCredentials adminCredentials;

	private Credentials credentials;

	private String createUserUrl;

	private String updateUserUrl;

	private String clearSessionUrl;

	private String logoutUrl;

	private String userNameUrl;

	private String resetPasswordUrl;

	private String updatePasswordLifespan;

	private String forgotPasswordUrl;

	private String updatePasswordRedirectUri;

	private String allUsers;

	private String roleDetails;

	private String userRoleMappings;

	private String userById;

	private String userByRoleUsername;

	private String userByUsername;

	@Data
	@NoArgsConstructor
	public static class AdminCredentials {
		private String username;
		private String password;
	}

	@Data
	@NoArgsConstructor
	public static class Credentials {
		private Map<String, String> secrets;
	}
}
