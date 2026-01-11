package com.service.catalog.service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import com.common.dto.KeycloakuserDto;
import com.common.dto.PaginatedResponse;
import com.common.utils.KeycloakUtility;
import com.exception.handling.models.ValidationException;

public class AdminService {

	private static final String ADMIN_ROLE = "ADMIN";

	private KeycloakUtility keycloakUtility;

	public AdminService(KeycloakUtility keycloakUtility) {
		this.keycloakUtility = keycloakUtility;
	}

	public Map<String, String> createUser(KeycloakuserDto adminRequestDto, String superAdminId, String realm) {
		if (ObjectUtils.isEmpty(adminRequestDto.getUsername())) {
			throw new ValidationException("Username is required");
		}
		if (ObjectUtils.isEmpty(adminRequestDto.getEmail())) {
			throw new ValidationException("Email is required");
		}
		String phoneNumber = extractPhoneNumber(adminRequestDto);
		if (ObjectUtils.isEmpty(phoneNumber)) {
			throw new ValidationException("Phone number is required");
		}
		List<UserRepresentation> usersByUsername = keycloakUtility.userByUsername(adminRequestDto.getUsername(),
				ADMIN_ROLE, realm);
		if (!CollectionUtils.isEmpty(usersByUsername)) {
			throw new ValidationException("Username already exists for role " + ADMIN_ROLE);
		}
		List<UserRepresentation> usersByEmail = keycloakUtility.userByEmailAndRole(adminRequestDto.getEmail(),
				ADMIN_ROLE, realm);
		if (!CollectionUtils.isEmpty(usersByEmail)) {
			throw new ValidationException("Email already exists for role " + ADMIN_ROLE);
		}
		List<UserRepresentation> usersByPhone = keycloakUtility.userByPhoneAndRole(phoneNumber, ADMIN_ROLE, realm);
		if (!CollectionUtils.isEmpty(usersByPhone)) {
			throw new ValidationException("Phone number already exists for role " + ADMIN_ROLE);
		}
		return keycloakUtility.createUser(adminRequestDto, ADMIN_ROLE, realm);
	}

	/**
	 * Extracts phone number from Keycloak attributes
	 */
	private String extractPhoneNumber(KeycloakuserDto dto) {
		if (ObjectUtils.isEmpty(dto.getAttributes())) {
			return null;
		}

		List<String> phoneList = dto.getAttributes().get("phoneNumber");
		return CollectionUtils.isEmpty(phoneList) ? null : phoneList.get(0);
	}

	public UserRepresentation getAdminById(String adminId, String realm) {
		UserRepresentation user = keycloakUtility.userById(adminId, realm);
		if (user == null) {
			throw new ValidationException("Admin not found");
		}
		return user;
	}

	public PaginatedResponse<UserRepresentation> getAllAdmins(Map<String, Object> allParams, String realm) {
		Object isEnabledObj = allParams.get("isEnabled");
		String isEnabled = (isEnabledObj != null) ? isEnabledObj.toString().toUpperCase() : "ALL";

		int page = allParams.containsKey("page") ? Integer.parseInt(allParams.get("page").toString()) : 0;
		int size = allParams.containsKey("size") ? Integer.parseInt(allParams.get("size").toString()) : 10;

		// Fetch all admins for the given realm and role
		List<UserRepresentation> admins = keycloakUtility.allUsersOfSpecificRoleAndRealm(ADMIN_ROLE, allParams, realm);

		// Filter by enabled status
		if ("TRUE".equals(isEnabled)) {
			admins = admins.stream().filter(UserRepresentation::isEnabled).toList();
		} else if ("FALSE".equals(isEnabled)) {
			admins = admins.stream().filter(user -> !user.isEnabled()).toList();
		}

		// Sort by created timestamp descending, nulls last
		admins = admins.stream().sorted(Comparator
				.comparing(UserRepresentation::getCreatedTimestamp, Comparator.nullsLast(Long::compareTo)).reversed())
				.toList();

		// Pagination
		int fromIndex = Math.min(page * size, admins.size());
		int toIndex = Math.min(fromIndex + size, admins.size());
		List<UserRepresentation> paginatedAdmins = admins.subList(fromIndex, toIndex);

		long totalElements = admins.size();
		return new PaginatedResponse<>(paginatedAdmins, totalElements, page, size);
	}

}
