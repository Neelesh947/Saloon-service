package com.service.catalog.service;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.common.dto.UserRequestDTO;
import com.common.utils.KeycloakUtility;
import com.exception.handling.models.ValidationException;
import com.service.catalog.repository.UserServiceMappingRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private static final String USER_ROLE = "USER";

	private final KeycloakUtility keycloakUtility;
	private final UserServiceMappingRepository serivceMappingRepository;

	public Map<String, String> createUser(UserRequestDTO dto, String realm) {
		validatingUser(dto, realm);
		Map<String, String> response = keycloakUtility.createUser(dto, USER_ROLE, realm);
		if (!"success".equalsIgnoreCase(response.get("status"))) {
			throw new ValidationException("User creation failed");
		}
		return Map.of("status", "success", "message", "User created successfully", "userId", response.get("userId"));
	}

	private void validatingUser(UserRequestDTO dto, String realm) {
		if (dto == null) {
			throw new ValidationException("Request body is required");
		}
		if (ObjectUtils.isEmpty(dto.getUsername())) {
			throw new ValidationException("Username is required");
		}
		if (ObjectUtils.isEmpty(dto.getEmail())) {
			throw new ValidationException("Email is required");
		}
		if (ObjectUtils.isEmpty(dto.getPassword())) {
			throw new ValidationException("Password is required");
		}
		if (!keycloakUtility.userByUsername(dto.getUsername(), USER_ROLE, realm).isEmpty()) {
			throw new ValidationException("Username already exists");
		}
		if (!keycloakUtility.userByEmailAndRole(dto.getEmail(), USER_ROLE, realm).isEmpty()) {
			throw new ValidationException("Email already exists");
		}
		if (!ObjectUtils.isEmpty(dto.getPhone())) {
			if (!keycloakUtility.userByPhoneAndRole(dto.getPhone(), USER_ROLE, realm).isEmpty()) {
				throw new ValidationException("Phone number already exists");
			}
		}
	}
}
