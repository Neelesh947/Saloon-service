package com.service.catalog.service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.common.dto.PaginatedResponse;
import com.common.dto.UserRequestDTO;
import com.common.dto.UserResponseDTO;
import com.common.entity.SaloonService;
import com.common.entity.UserServiceMapping;
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

	public PaginatedResponse<UserResponseDTO> getAllUsers(Map<String, Object> allParams, String realm) {
		Object isEnabledObj = allParams.get("isEnabled");
		String isEnabled = (isEnabledObj != null) ? isEnabledObj.toString().toUpperCase() : "ALL";
		int page = allParams.containsKey("page") ? Integer.parseInt(allParams.get("page").toString()) : 0;
		int size = allParams.containsKey("size") ? Integer.parseInt(allParams.get("size").toString()) : 10;
		List<UserRepresentation> users = keycloakUtility.allUsersOfSpecificRoleAndRealm(USER_ROLE, allParams, realm);

		if ("TRUE".equals(isEnabled)) {
			users = users.stream().filter(UserRepresentation::isEnabled).toList();
		} else if ("FALSE".equals(isEnabled)) {
			users = users.stream().filter(user -> !user.isEnabled()).toList();
		}
		users = users.stream().sorted(Comparator
				.comparing(UserRepresentation::getCreatedTimestamp, Comparator.nullsLast(Long::compareTo)).reversed())
				.toList();
		int fromIndex = Math.min(page * size, users.size());
		int toIndex = Math.min(fromIndex + size, users.size());
		List<UserRepresentation> paginatedUsers = users.subList(fromIndex, toIndex);
		List<UserResponseDTO> responseList = paginatedUsers.stream().map(this::mapToUserResponseDTO)
				.collect(Collectors.toList());

		return new PaginatedResponse<>(responseList, users.size(), page, size);
	}

	private UserResponseDTO mapToUserResponseDTO(UserRepresentation user) {
		UserResponseDTO dto = new UserResponseDTO();
		dto.setKeycloakUserId(user.getId());
		dto.setFirstName(user.getFirstName());
		dto.setLastName(user.getLastName());
		dto.setEmail(user.getEmail());
		dto.setAttributes(user.getAttributes());

		List<UserServiceMapping> mappings = serivceMappingRepository.findByLinkedUserId(UUID.fromString(user.getId()));
		List<SaloonService> serviceIds = mappings.stream().map(UserServiceMapping::getLinkedServiceId).toList();

		dto.setBookedServiceIds(serviceIds);
		return dto;
	}

	public UserResponseDTO getUserById(String id, String realm) {
		if (ObjectUtils.isEmpty(id)) {
			throw new ValidationException("User ID is required");
		}
		UserRepresentation user = keycloakUtility.userById(id, realm);
		if (user == null) {
			throw new ValidationException("User not found with ID: " + id);
		}
		return mapToUserResponseDTO(user);
	}

	public UserResponseDTO updateUser(String id, UserRequestDTO dto, String realm) {
		if (ObjectUtils.isEmpty(id)) {
			throw new ValidationException("User ID is required");
		}
		if (dto == null) {
			throw new ValidationException("Request body is required");
		}
		UserRepresentation existingUser = keycloakUtility.userById(id, realm);
		if (existingUser == null) {
			throw new ValidationException("User not found with ID: " + id);
		}

		if (!ObjectUtils.isEmpty(dto.getUsername()) && !dto.getUsername().equals(existingUser.getUsername())) {
			if (!keycloakUtility.userByUsername(dto.getUsername(), USER_ROLE, realm).isEmpty()) {
				throw new ValidationException("Username already exists");
			}
			existingUser.setUsername(dto.getUsername());
		}
		if (!ObjectUtils.isEmpty(dto.getEmail()) && !dto.getEmail().equals(existingUser.getEmail())) {
			if (!keycloakUtility.userByEmailAndRole(dto.getEmail(), USER_ROLE, realm).isEmpty()) {
				throw new ValidationException("Email already exists");
			}
			existingUser.setEmail(dto.getEmail());
		}
		if (!ObjectUtils.isEmpty(dto.getPhone())) {
			List<UserRepresentation> phoneUsers = keycloakUtility.userByPhoneAndRole(dto.getPhone(), USER_ROLE, realm);
			if (!phoneUsers.isEmpty() && !phoneUsers.get(0).getId().equals(id)) {
				throw new ValidationException("Phone number already exists");
			}
			existingUser.getAttributes().put("phone", List.of(dto.getPhone()));
		}

		if (!ObjectUtils.isEmpty(dto.getFirstName())) {
			existingUser.setFirstName(dto.getFirstName());
		}
		if (!ObjectUtils.isEmpty(dto.getLastName())) {
			existingUser.setLastName(dto.getLastName());
		}
		keycloakUtility.updateUser(existingUser, id, realm);
		return mapToUserResponseDTO(keycloakUtility.userById(id, realm));
	}
	
//	public void deleteUser(String id, String realm) {
//        if (ObjectUtils.isEmpty(id)) {
//            throw new ValidationException("User ID is required");
//        }
//
//        UserRepresentation user = keycloakUtility.userById(id, realm);
//        if (user == null) {
//            throw new ValidationException("User not found with ID: " + id);
//        }
//
//        List<UserServiceMapping> mappings = serivceMappingRepository.findByLinkedUserId(UUID.fromString(id));
//        serivceMappingRepository.deleteAll(mappings);
//        // Delete user from Keycloak
//        keycloakUtility.deleteUser(id, realm);
//    }

}
