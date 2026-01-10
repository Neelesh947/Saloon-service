package com.keycloak.utils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Component;

import com.keycloak.dto.KeycloakUserDto;

@Component
public class KeycloakObjectMapper {

	private Function<String, CredentialRepresentation> createCredentialRepresentation = password -> {
		CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
		credentialRepresentation.setTemporary(false);
		credentialRepresentation.setType("PASSWORD");
		credentialRepresentation.setValue(password);
		return credentialRepresentation;
	};

	// Function to create a list of credential representations from user input
	private Function<KeycloakUserDto, List<CredentialRepresentation>> credentialRepresentation = user -> Collections
			.singletonList(createCredentialRepresentation.apply(user.getPassword()));

	private void addAttribute(Map<String, List<String>> attributes, String key, List<String> value) {
		if (value != null && !value.isEmpty()) {
			attributes.put(key, value);
		}
	}

	private BiFunction<KeycloakUserDto, String, Map<String, List<String>>> createUserAttributes = (userDTO, role) -> {
		Map<String, List<String>> attributes = new HashMap<>();
		addAttribute(attributes, Constants.ROLES, UtilityFunctions.toListOrNull(role));
		addAttribute(attributes, Constants.PHONE_NUMBER, UtilityFunctions.toListOrNull(userDTO.getPhoneNumber()));
		addAttribute(attributes, Constants.COMPANY_NAME, UtilityFunctions.toListOrNull(userDTO.getCompanyName()));
		addAttribute(attributes, Constants.ADDRESS, UtilityFunctions.toListOrNull(userDTO.getAddress()));
		addAttribute(attributes, Constants.CITY, UtilityFunctions.toListOrNull(userDTO.getCity()));
		addAttribute(attributes, Constants.POSTAL_CODE, UtilityFunctions.toListOrNull(userDTO.getPostalCode()));
		addAttribute(attributes, Constants.STATE, UtilityFunctions.toListOrNull(userDTO.getState()));
		addAttribute(attributes, Constants.COUNTRY, UtilityFunctions.toListOrNull(userDTO.getCountry()));
		addAttribute(attributes, Constants.CUSTOMER_SUPPORT_NUMBER,
				UtilityFunctions.toListOrNull(userDTO.getCustomerSupportNumber()));
		addAttribute(attributes, Constants.ASSIGNED_SENDERS,
				UtilityFunctions.toListOrNull(userDTO.getAssignedSenders()));
		addAttribute(attributes, Constants.COUNTRY_CODE, UtilityFunctions.toListOrNull(userDTO.getCountryCode()));
		addAttribute(attributes, Constants.CREATED_BY,
				UtilityFunctions.toListOrNull(String.valueOf(userDTO.getCreatedBy())));
		return attributes;
	};

	public final BiFunction<KeycloakUserDto, String, UserRepresentation> keycloakUserRepresentation = (userDTO,
			role) -> {
		UserRepresentation userRepresentation = new UserRepresentation();
		userRepresentation.setFirstName(userDTO.getFirstName());
		userRepresentation.setLastName(userDTO.getLastName());
		userRepresentation.setEmailVerified(false);
		userRepresentation.setEnabled(userDTO.isEnabled());
		userRepresentation.setUsername(userDTO.getUsername());
		userRepresentation.setEmail(userDTO.getEmailAddress());
		userRepresentation.setCredentials(credentialRepresentation.apply(userDTO));
		userRepresentation.setAttributes(createUserAttributes.apply(userDTO, role));
		return userRepresentation;
	};
}
