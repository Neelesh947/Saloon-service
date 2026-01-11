package com.common.dto;

import java.util.List;
import java.util.Map;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KeycloakuserDto {

	@NotBlank(message = "Username is required")
	private String username;
	
	@NotBlank(message = "Email is required")
	@Email(message = "Email format is invalid")
	private String email;
	
	private String firstName;
	private String lastName;
	private boolean isEnabled;
	private List<Credentials> credentials;
	private Map<String, List<String>> attributes;
}
