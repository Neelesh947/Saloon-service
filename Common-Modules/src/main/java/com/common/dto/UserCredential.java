package com.common.dto;

import com.fasterxml.jackson.annotation.JsonAlias;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserCredential {

	@JsonAlias({ "username", "userName" })
	private String username;

	private String password;
	
	private String userType;
}