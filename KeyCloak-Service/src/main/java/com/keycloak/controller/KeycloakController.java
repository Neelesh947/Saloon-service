package com.keycloak.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.keycloak.dto.LoginDto;
import com.keycloak.dto.TokenResponseDto;
import com.keycloak.service.KeycloakService;

@RestController
@RequestMapping("/{realm}/keycloak")
public class KeycloakController {

	private final KeycloakService keycloakService;

	public KeycloakController(KeycloakService keycloakService) {
		this.keycloakService = keycloakService;
	}

	@PostMapping("/login")
	public ResponseEntity<TokenResponseDto> login(@RequestBody LoginDto loginDto, @PathVariable String realm) {
		TokenResponseDto token = keycloakService.login(loginDto, realm);
		return ResponseEntity.ok(token);
	}

	@PostMapping("/create/user")
	public ResponseEntity<Map<String, String>> createUser(@RequestBody Object userObject, @RequestParam String role,
			@PathVariable String realm) {
		Map<String, String> response = keycloakService.createUser(userObject, role, realm);
	    return ResponseEntity.ok(response);
	}
}
