package com.common.dto;

import lombok.Data;

@Data
public class UserRequestDTO {

	private String firstName;
	private String lastName;
	private String email;
	private String phone;
	private String username;
	private String address;
	private String password;
	private boolean enabled;
}
