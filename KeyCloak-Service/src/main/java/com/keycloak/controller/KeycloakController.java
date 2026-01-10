package com.keycloak.controller;

import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
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

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

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

	/**
	 * Handles the logout operation for a user by invalidating the provided refresh
	 * token. Logs the process status whether successful or error occurred during
	 * the logout process.
	 * 
	 * @param refreshToken  The refresh token of the user to be logged out, provided
	 *                      in the request header.
	 * @param realm         The realm or domain under which the user is associated,
	 *                      specified as a path variable.
	 * @param terminalAdmin A flag indicating whether the logout request is for a
	 *                      terminal admin session. The default value is false.
	 */
	@PostMapping("/logout")
	public void logout(@RequestHeader("refresh-token") String refreshToken, @PathVariable String realm) {
		try {
			keycloakService.logout.accept(refreshToken, realm);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Resets the password for a specified user within the given realm. The method
	 * validates the user credentials, sets the realm, and delegates the password
	 * reset process to the keycloak service. Logs success or error messages based
	 * on the outcome.
	 * 
	 * @param userCredential the user credentials containing username and password
	 *                       details
	 * @param realm          the realm in which the user's password is to be reset
	 */
	@PutMapping("/reset-password")
	public void resetUserPassword(@Validated @RequestBody UserCredentialDTO userCredential,
			@PathVariable String realm) {
		userCredential.setRealm(realm);
		try {
			keycloakService.resetPassword.accept(userCredential);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Handles the request to send a forgot password link to the user. This method
	 * validates the user credentials and triggers the process of sending a password
	 * reset link to the user's registered email address.
	 * 
	 * @param userCredential the user's credentials required to request a password
	 *                       reset
	 * @param realm          the realm or domain associated with the user
	 */
	@PutMapping("/forgot-password")
	public void sendForgotPasswordLink(@Validated @RequestBody UserCredentialDTO userCredential,
			@PathVariable String realm) {
		userCredential.setRealm(realm);
		try {
			keycloakService.forgotPassword.accept(userCredential);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Handles the creation of a Keycloak user for a specified realm and role.
	 * 
	 * @param userDTO the data transfer object containing user details and
	 *                attributes required for creation
	 * @param realm   the realm to which the user belongs; must not be null or empty
	 * @param role    the role to assign to the user; must not be null or empty
	 * @return the response from the Keycloak service indicating the result of the
	 *         user creation
	 */
	@PostMapping("/create/user")
	public String createKeycloakUser(@RequestBody KeycloakUserDto userDTO,
			@NotBlank(message = "Realm must not be null or empty") @PathVariable String realm,
			@NotBlank(message = "Role must not be null or empty") @RequestParam String role) {
		try {
			String response = keycloakService.createUser.apply(userDTO, role, realm);
			return response;
		} catch (Exception e) {
			throw e;
		}
	}

	@PutMapping("/update/user/{userId}")
	public void updateKeycloakUser(@Valid @RequestBody UserRepresentation userRepresentationDTO,
			@NotBlank(message = "User ID must not be null or empty") @PathVariable String userId,
			@NotBlank(message = "Realm must not be null or empty") @PathVariable String realm) {
		try {
			keycloakService.updateUser.accept(userRepresentationDTO, userId, realm);
		} catch (Exception e) {
			throw e;
		}
	}

	@DeleteMapping("/delete/user/{userId}")
	public void deleteKeycloakUser(@NotBlank(message = "User ID must not be null or empty") @PathVariable String userId,
			@NotBlank(message = "Realm must not be null or empty") @PathVariable String realm) {
		try {
			keycloakService.deleteUser.accept(userId, realm);
		} catch (Exception e) {
			throw e;
		}
	}
	
	@GetMapping("/user/{userId}")
	public UserRepresentation keycloakUserById(
			@NotBlank(message = "User ID must not be null or empty") @PathVariable String userId,
			@NotBlank(message = "Realm must not be null or empty") @PathVariable String realm) {
		try {
			return keycloakService.userById.apply(userId, realm);
		} catch (Exception e) {
			throw e;
		}
	}
}
