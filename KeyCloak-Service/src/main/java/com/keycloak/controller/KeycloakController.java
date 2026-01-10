package com.keycloak.controller;

import java.util.Map;

import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.keycloak.dto.KeycloakUserDto;
import com.keycloak.dto.TokenResponseDto;
import com.keycloak.dto.UserCredentialDTO;
import com.keycloak.service.KeycloakService;

@RestController
@RequestMapping("/{realm}/keycloak")
public class KeycloakController {

	private final KeycloakService keycloakService;

	public KeycloakController(KeycloakService keycloakService) {
		this.keycloakService = keycloakService;
	}

	/**
	 * Authenticates users using their credentials and realm.
	 * 
	 * @param userCredential the credentials of the user to be authenticated
	 * @param realm          the realm to which the user belongs
	 * @return a TokenResponseDTO containing the authentication response, including
	 *         access and refresh tokens
	 */
	@PostMapping("/login")
	public TokenResponseDto terminalUsersLogin(@Validated @RequestBody UserCredentialDTO userCredential,
			@PathVariable String realm) {
		userCredential.setRealm(realm);
		try {
			TokenResponseDto response = keycloakService.loginUser.apply(userCredential);
			return response;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * End-point to refresh the access token using a provided refresh token.
	 * 
	 * @param refreshToken the refresh token provided in the request header
	 * @param realm        the realm identifier with which the request is associated
	 * @return KeycloakTokenResponseDTO containing the refreshed access token and
	 *         related information
	 */
	@GetMapping("/refresh-token")
	public TokenResponseDto refreshToken(@RequestHeader("refresh-token") String refreshToken,
			@PathVariable String realm) {
		try {
			TokenResponseDto response = keycloakService.refreshAccessToken.apply(refreshToken, realm);
			return response;
		} catch (Exception e) {
			throw e;
		}
	}

	@PostMapping("/create/user")
	public ResponseEntity<Map<String, String>> createUser(@RequestBody KeycloakUserDto userObject,
			@RequestParam String role, @PathVariable String realm) {
		Map<String, String> response = keycloakService.createUser(userObject, role, realm);
		return ResponseEntity.ok(response);
	}

	@PutMapping("/update/user/{userId}")
	public ResponseEntity<Void> updateUser(@RequestBody UserRepresentation userObject, @PathVariable String userId,
			@PathVariable String realm) {

		keycloakService.updateUser(userObject, userId, realm);
		return ResponseEntity.ok().build();
	}

}
