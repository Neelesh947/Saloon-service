package com.keycloak.dto;

import lombok.Data;

@Data
public class KeycloakUserDto {

	private String username;
	private String emailAddress;
	private String firstName;
	private String lastName;
	private String password;
	private boolean enabled;
	private String phoneNumber;
	private String companyName;
	private String address;
	private String city;
	private String postalCode;
	private String state;
	private String country;
	private String customerSupportNumber;
	private String assignedSenders;
	private String countryCode;
	private String createdBy;
}
