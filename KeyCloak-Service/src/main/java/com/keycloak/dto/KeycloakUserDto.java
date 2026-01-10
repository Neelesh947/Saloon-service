package com.keycloak.dto;

import java.util.List;
import java.util.Map;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class KeycloakUserDto {

	@NotBlank
	private String username;

	@NotBlank
	private String email;

	@NotBlank
	private String password;

	private String firstName;
	private String lastName;

	private Map<String, List<String>> attributes;
}
