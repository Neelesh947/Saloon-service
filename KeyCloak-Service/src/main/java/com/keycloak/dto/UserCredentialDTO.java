package com.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonAlias;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserCredentialDTO {

	@NotBlank(message = "userName is required")
	@JsonAlias({ "username", "userName" })
	private String userName;

	@Pattern(regexp = "\\S+", message = "Password must not be blank if present")
	private String password;

	private String email;

	@Pattern(regexp = "", message = "Invalid user Type")
	private String userType;

	private String realm;
}