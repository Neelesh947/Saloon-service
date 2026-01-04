package com.keycloak.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.keycloak.dto.LoginDto;
import com.keycloak.dto.TokenResponseDto;
import com.keycloak.utils.KeycloakHandler;

@Service
public class KeycloakService {

	private final KeycloakHandler keycloakHandler;

	private KeycloakService(KeycloakHandler keycloakHandler) {
		this.keycloakHandler = keycloakHandler;
	}

	public TokenResponseDto login(LoginDto loginDto, String realm) {
		return keycloakHandler.userAccessToken.apply(loginDto, realm);
	}

	public Map<String, String> createUser(Object userObject, String role, String realm) {
		return keycloakHandler.createUserFn.apply(new Object[] { userObject, role, realm }, null);
	}

}
