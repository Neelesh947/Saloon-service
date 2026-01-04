package com.common.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.util.InternalException;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class KeycloakUtility {

	String keycloakUrl = "http://localhost:8106/";

	RestTemplate restTemplate = new RestTemplate();

	public <T> Map<String, String> createUser(T userObject, String role, String realm) {
		Map<String, String> responseMap = new HashMap<>();
		String urlEndpoint = keycloakUrl + realm + "/keycloak/create/user?role=" + role;
		HttpEntity<T> reqMap = new HttpEntity<>(userObject);
		try {
			ResponseEntity<String> response = restTemplate.exchange(urlEndpoint, HttpMethod.POST, reqMap, String.class);
			if (response.getStatusCode().equals(HttpStatus.OK) && response.getBody() != null) {
				String userId = response.getBody();
				log.info("user created with id: {}", userId);
				responseMap.put("status", "success");
				responseMap.put("message", "User Created");
				responseMap.put("userId", userId);
				return responseMap;
			} else {
				throw new InternalException("User creation failed");
			}
		} catch (Exception e) {
			throw new InternalException(e);
		}
	}

	public void updateUser(UserRepresentation userObject, String userId, String realm) {
		String urlEndpoint = keycloakUrl + realm + "/keycloak/update/user/" + userId;
		HttpEntity<UserRepresentation> reqMap = new HttpEntity<>(userObject);
		try {
			restTemplate.exchange(urlEndpoint, HttpMethod.PUT, reqMap, void.class);
		} catch (Exception e) {
			throw new InternalException(e);
		}
	}

	public List<UserRepresentation> userByPhoneAndRole(String phoneNumber, String role, String realm) {
		String urlEndpoint = keycloakUrl + realm + "/keycloak/user/by/phone" + phoneNumber + "/" + role;
		try {
			ResponseEntity<List<UserRepresentation>> response = restTemplate.exchange(urlEndpoint, HttpMethod.GET, null,
					new ParameterizedTypeReference<List<UserRepresentation>>() {
					});
			return response.getBody();
		} catch (Exception e) {
			throw new InternalException(e);
		}
	}
	
	//userByEmailAndRole list
	//UserByUserName list
	//UserById
	//allUsersOfSpecificRoleAndRealm list
	//usersByRoleOfAllRealm list
	//validateUsername
	//fetchUserAcrossRealmsByRoleAndId
}
