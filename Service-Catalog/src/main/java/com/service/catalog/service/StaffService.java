package com.service.catalog.service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import com.common.dto.PaginatedResponse;
import com.common.dto.StaffRequestDTO;
import com.common.dto.StaffResponseDTO;
import com.common.entity.AdminAndStaffMapping;
import com.common.utils.KeycloakUtility;
import com.exception.handling.models.ValidationException;
import com.service.catalog.repository.AdminAndStaffMappingRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StaffService {

	private static final String STAFF_ROLE = "STAFF";

	private final KeycloakUtility keycloakUtility;
	private final AdminAndStaffMappingRepository mappingRepository;

	@Transactional
	public Map<String, String> createStaff(StaffRequestDTO staffDto, String adminId, String realm) {
		validateStaffDto(staffDto, realm);
		Map<String, String> response = keycloakUtility.createUser(staffDto, STAFF_ROLE, realm);
		String staffKeycloakId = response.get("userId");
		AdminAndStaffMapping mapping = new AdminAndStaffMapping();
		mapping.setLinkedAdminId(adminId);
		mapping.setLinkedStaffId(staffKeycloakId);
		mappingRepository.save(mapping);
		response.put("status", "success");
		response.put("message", "Staff created successfully");
		return response;
	}

	private void validateStaffDto(StaffRequestDTO staffDto, String realm) {
		if (ObjectUtils.isEmpty(staffDto.getName()))
			throw new ValidationException("Name required");
		if (ObjectUtils.isEmpty(staffDto.getEmail()))
			throw new ValidationException("Email required");

		List<UserRepresentation> users = keycloakUtility.userByEmailAndRole(staffDto.getEmail(), "STAFF", realm);
		if (!CollectionUtils.isEmpty(users))
			throw new ValidationException("Email already exists");

		String phone = staffDto.getPhone();
		if (!ObjectUtils.isEmpty(phone)) {
			List<UserRepresentation> usersByPhone = keycloakUtility.userByPhoneAndRole(phone, "STAFF", realm);
			if (!CollectionUtils.isEmpty(usersByPhone))
				throw new ValidationException("Phone already exists");
		}
	}

	public PaginatedResponse<StaffResponseDTO> getAllStaff(Map<String, Object> allParams, String adminId,
			String realm) {
		Object isEnabledObj = allParams.get("isEnabled");
		String isEnabled = (isEnabledObj != null) ? isEnabledObj.toString().toUpperCase() : "ALL";

		int page = allParams.containsKey("page") ? Integer.parseInt(allParams.get("page").toString()) : 0;
		int size = allParams.containsKey("size") ? Integer.parseInt(allParams.get("size").toString()) : 10;

		List<UserRepresentation> staffUsers = keycloakUtility.allUsersOfSpecificRoleAndRealm(STAFF_ROLE, allParams,
				realm);
		if ("TRUE".equals(isEnabled)) {
			staffUsers = staffUsers.stream().filter(UserRepresentation::isEnabled).toList();
		} else if ("FALSE".equals(isEnabled)) {
			staffUsers = staffUsers.stream().filter(u -> !u.isEnabled()).toList();
		}
		staffUsers = staffUsers.stream().sorted(Comparator
				.comparing(UserRepresentation::getCreatedTimestamp, Comparator.nullsLast(Long::compareTo)).reversed())
				.toList();
		int fromIndex = Math.min(page * size, staffUsers.size());
		int toIndex = Math.min(fromIndex + size, staffUsers.size());
		List<UserRepresentation> paginatedUsers = staffUsers.subList(fromIndex, toIndex);
		List<StaffResponseDTO> staffDTOs = paginatedUsers.stream().map(this::mapToDto).collect(Collectors.toList());

		return new PaginatedResponse<>(staffDTOs, staffUsers.size(), page, size);
	}

	private StaffResponseDTO mapToDto(UserRepresentation user) {
		StaffResponseDTO dto = new StaffResponseDTO();
		dto.setKeycloakUserId(user.getId());
		dto.setName(user.getFirstName());
		dto.setEmail(user.getEmail());
		dto.setPhone(user.getAttributes() != null && user.getAttributes().containsKey("phone")
				? user.getAttributes().get("phone").get(0)
				: null);

		List<String> serviceIdsStr = CollectionUtils
				.isEmpty(user.getAttributes() != null ? user.getAttributes().get("serviceIds") : null) ? List.of()
						: user.getAttributes().get("serviceIds");

		List<java.util.UUID> serviceIds = serviceIdsStr.stream().map(java.util.UUID::fromString)
				.collect(Collectors.toList());
		dto.setServiceIds(serviceIds);

		return dto;
	}

	public StaffResponseDTO getStaffById(String staffId, String realm) {
		UserRepresentation user = keycloakUtility.userById(staffId, realm);
		if (user == null)
			throw new ValidationException("Staff not found");
		return mapToDto(user);
	}

	public StaffResponseDTO updateStaff(String staffId, StaffRequestDTO dto, String realm) {

		UserRepresentation user = keycloakUtility.userById(staffId, realm);
		if (user == null)
			throw new ValidationException("Staff not found");

		// Validate service IDs if provided
		if (!CollectionUtils.isEmpty(dto.getServiceIds()))
			validateServiceIds(dto.getServiceIds());

		// Update fields
		if (dto.getName() != null)
			user.setFirstName(dto.getName());
		if (dto.getEmail() != null)
			user.setEmail(dto.getEmail());

		Map<String, List<String>> attributes = user.getAttributes() != null ? user.getAttributes() : new HashMap<>();
		if (dto.getPhone() != null)
			attributes.put("phone", List.of(dto.getPhone()));
		if (!CollectionUtils.isEmpty(dto.getServiceIds()))
			attributes.put("serviceIds", dto.getServiceIds().stream().map(UUID::toString).toList());
		user.setAttributes(attributes);

		keycloakUtility.updateUser(user, staffId, realm);

		return mapToDto(user);
	}

	// Enable/disable staff
	public void changeStaffStatus(String staffId, boolean enable, String realm) {
		UserRepresentation user = keycloakUtility.userById(staffId, realm);
		if (user == null)
			throw new ValidationException("Staff not found");
		user.setEnabled(enable);
		keycloakUtility.updateUser(user, staffId, realm);
	}

	private void validateServiceIds(List<UUID> serviceIds) {
		if (serviceIds == null || serviceIds.isEmpty())
			return;
	}
}
