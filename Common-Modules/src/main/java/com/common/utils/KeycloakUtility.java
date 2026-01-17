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
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

import com.common.constants.AppConstant;
import com.common.dto.UserCredential;
import com.exception.handling.models.ValidationException;

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
				log.info("User Created with id: {}", userId);
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
		String urlEndpoint = keycloakUrl + realm + "/keycloak/user/by/phone/" + phoneNumber + "/" + role;
		try {
			ResponseEntity<List<UserRepresentation>> response = restTemplate.exchange(urlEndpoint, HttpMethod.GET, null,
					new ParameterizedTypeReference<List<UserRepresentation>>() {
					});
			return response.getBody();
		} catch (Exception e) {
			throw new InternalException(e);
		}
	}

	// userByEmailAndRole list
	public List<UserRepresentation> userByEmailAndRole(String email, String role, String realm) {
		String urlEndpoint = keycloakUrl + realm + "/keycloak/user/by/email/" + email + "/" + role;

		try {
			ResponseEntity<List<UserRepresentation>> response = restTemplate.exchange(urlEndpoint, HttpMethod.GET, null,
					new ParameterizedTypeReference<List<UserRepresentation>>() {
					});
			return response.getBody();
		} catch (Exception e) {
			throw new InternalException(e);
		}
	}

	// UserByUserName list
	public List<UserRepresentation> userByUsername(String username, String role, String realm) {
		String urlEndpoint = !ObjectUtils.isEmpty(role)
				? keycloakUrl + realm + "/keycloak/user/by/username/" + username + "?role=" + role
				: keycloakUrl + realm + "/keycloak/user/by/username/" + username;
		try {
			ResponseEntity<List<UserRepresentation>> response = restTemplate.exchange(urlEndpoint, HttpMethod.GET, null,
					new ParameterizedTypeReference<List<UserRepresentation>>() {
					});
			return response.getBody();
		} catch (Exception e) {
			throw new InternalException(e);
		}
	}

	// UserById
	public UserRepresentation userById(String userId, String realm) {
		String urlEndpoint = keycloakUrl + realm + "/keycloak/user/" + userId;

		try {
			ResponseEntity<UserRepresentation> response = restTemplate.exchange(urlEndpoint, HttpMethod.GET, null,
					UserRepresentation.class);
			return response.getBody();
		} catch (Exception e) {
			throw new InternalException(e);
		}
	}

	// allUsersOfSpecificRoleAndRealm list
	public List<UserRepresentation> allUsersOfSpecificRoleAndRealm(String role, Map<String, Object> allParams,
			String realm) {
		String urlEndpoint = keycloakUrl + realm + "/keycloak/users/by/role/" + role;
		HttpEntity<Map<String, Object>> reqMap = new HttpEntity<>(allParams != null ? allParams : new HashMap<>());
		try {
			ResponseEntity<List<UserRepresentation>> response = restTemplate.exchange(urlEndpoint, HttpMethod.POST,
					reqMap, new ParameterizedTypeReference<List<UserRepresentation>>() {
					});
			return response.getBody();
		} catch (Exception e) {
			throw new InternalException(e);
		}
	}

	// usersByRoleOfAllRealm list
	public Map<String, List<UserRepresentation>> usersByRoleOfAllRealm(String role) {
		String urlEndpoint = keycloakUrl + "keycloak/users/by/role/all-realms/" + role;

		try {
			ResponseEntity<Map<String, List<UserRepresentation>>> response = restTemplate.exchange(urlEndpoint,
					HttpMethod.GET, null, new ParameterizedTypeReference<Map<String, List<UserRepresentation>>>() {
					});
			return response.getBody();
		} catch (Exception e) {
			throw new InternalException(e);
		}
	}

	public ResponseEntity<Void> resetPassword(UserCredential userData, String realm) {
		String urlEndpoint = keycloakUrl + realm + "/keycloak/reset-password";
		HttpEntity<UserCredential> reqMap = new HttpEntity<>(userData);
		try {
			restTemplate.exchange(urlEndpoint, HttpMethod.PUT, reqMap, void.class);
			return ResponseEntity.ok().build();
		} catch (Exception ex) {
			log.error("Exception occured :", ex);
			throw new InternalException(ex);
		}
	}

	// validateUsername
	public ResponseEntity<Map<String, String>> validateUserName(UserCredential userCredential, String realm) {
		UserRepresentation user = userByUsername(userCredential.getUsername(), userCredential.getUserType(), realm)
				.stream().findFirst().orElseThrow(() -> new ValidationException("User Not Found"));

		if (!user.isEnabled()) {
			throw new ValidationException("User is Dissabled");
		}
		Map<String, String> responseMap = new HashMap<>();
		responseMap.put(AppConstant.STATUS_KEY, AppConstant.SUCCESS);
		responseMap.put(AppConstant.MESSAGE, AppConstant.VALID_USERNAME);
		responseMap.put(AppConstant.ID, user.getId());
		return new ResponseEntity<Map<String, String>>(responseMap, HttpStatus.OK);
	}

	// fetchUserAcrossRealmsByRoleAndId
	public UserRepresentation fetchUserAcrossRealmsByRoleAndId(String userId, String role, String realm) {
		realm = ObjectUtils.isEmpty(realm) ? "Saloon" : realm;
		String urlEndpoint = keycloakUrl + "keycloak/user/by/id/role/" + userId + "/" + role;

		try {
			ResponseEntity<List<UserRepresentation>> response = restTemplate.exchange(urlEndpoint, HttpMethod.GET, null,
					new ParameterizedTypeReference<List<UserRepresentation>>() {
					});
			List<UserRepresentation> allUsers = response.getBody();
			return allUsers.stream().filter(u -> userId.equals(u.getId())).findFirst().orElse(null);
		} catch (Exception e) {
			throw new InternalException(e);
		}
	}
}
