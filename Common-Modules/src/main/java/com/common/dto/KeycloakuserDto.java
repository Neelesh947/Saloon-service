package com.common.dto;

import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KeycloakuserDto {

	private String username;
	private String email;
	private String firstName;
	private String lastName;
	private boolean isEnabled;	
	private List<Credentials> credentials;
	private Map<String, List<String>> attributes;
//	private String realm;
}
