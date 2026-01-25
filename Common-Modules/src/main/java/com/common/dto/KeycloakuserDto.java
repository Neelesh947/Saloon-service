package com.common.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class KeycloakuserDto {

	@NotBlank(message = "Username is required")
	private String username;
	
	@NotBlank(message = "Email is required")
	@Email(message = "Email format is invalid")
	private String emailAddress;
	private String password;
	private String firstName;
	private String lastName;
	private boolean enabled;
	private String phoneNumber;
	private String address;
	private String createdBy;
	
}
