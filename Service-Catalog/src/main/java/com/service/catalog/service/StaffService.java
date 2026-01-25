package com.service.catalog.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import com.common.entity.Staff;
import com.common.utils.KeycloakUtility;
import com.exception.handling.models.ValidationException;
import com.service.catalog.repository.AdminAndStaffMappingRepository;
import com.service.catalog.repository.StaffRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StaffService {

	private static final String STAFF_ROLE = "STAFF";

	private final KeycloakUtility keycloakUtility;
	private final AdminAndStaffMappingRepository mappingRepository;
	private final StaffRepository staffRepository;

	@Transactional
	public Map<String, String> createStaff(StaffRequestDTO staffDto, String adminId, String realm) {
		validateStaffDto(staffDto, realm);
		Map<String, String> response = keycloakUtility.createUser(staffDto, STAFF_ROLE, realm);
		String staffKeycloakId = response.get("userId");
		AdminAndStaffMapping mapping = new AdminAndStaffMapping();
		mapping.setLinkedAdminId(adminId);
		mapping.setLinkedStaffId(staffKeycloakId);
		mappingRepository.save(mapping);
		mapUserInDb(response, staffDto, adminId);
		response.put("status", "success");
		response.put("message", "Staff created successfully");
		return response;
	}

	@Transactional
	private void mapUserInDb(Map<String, String> response, StaffRequestDTO dto, String adminId) {
		Staff staff = new Staff();
		staff.setCreatedBy(adminId);
		staff.setEmail(dto.getEmailAddress());
		staff.setKeycloakUserId(response.get("userId"));
		staff.setName(dto.getFirstName() + " " + dto.getLastName());
		staff.setPhone(dto.getPhone());
		if (dto.getServiceIds() != null && !dto.getServiceIds().isEmpty()) {
			List<String> serviceUuids = dto.getServiceIds();
			staff.setServiceIds(serviceUuids);
		}
		staffRepository.save(staff);
	}

	private void validateStaffDto(StaffRequestDTO staffDto, String realm) {
		if (ObjectUtils.isEmpty(staffDto.getFirstName()))
			throw new ValidationException("First Name required");
		if (ObjectUtils.isEmpty(staffDto.getLastName()))
			throw new ValidationException("Last Name required");
		if (ObjectUtils.isEmpty(staffDto.getUsername()))
			throw new ValidationException("UserName required");
		if (ObjectUtils.isEmpty(staffDto.getEmailAddress()))
			throw new ValidationException("Email required");

		List<UserRepresentation> users = keycloakUtility.userByEmailAndRole(staffDto.getEmailAddress(), "STAFF", realm);
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

		List<Staff> staffList = staffRepository.findByCreatedBy(adminId);
		List<Staff> filteredStaff = staffList.stream().filter(staff -> {
			if ("TRUE".equals(isEnabled)) {
				return staff.isEnabled();
			} else if ("FALSE".equals(isEnabled)) {
				return !staff.isEnabled();
			} else {
				return true; // ALL
			}
		}).collect(Collectors.toList());
		int fromIndex = Math.min(page * size, filteredStaff.size());
		int toIndex = Math.min(fromIndex + size, filteredStaff.size());
		List<Staff> paginatedStaff = filteredStaff.subList(fromIndex, toIndex);
		List<StaffResponseDTO> staffDTOs = paginatedStaff.stream().map(this::mapToDtoStaff).collect(Collectors.toList());

		return new PaginatedResponse<>(staffDTOs, filteredStaff.size(), page, size);
	}

	private StaffResponseDTO mapToDtoStaff(Staff staff) {
		    StaffResponseDTO dto = new StaffResponseDTO();
		    dto.setId(staff.getId());
		    dto.setCreatedAt(staff.getCreatedAt());
		    dto.setUpdatedAt(staff.getUpdatedAt());
		    dto.setName(staff.getName());
		    dto.setEmail(staff.getEmail());
		    dto.setPhone(staff.getPhone());
		    dto.setKeycloakUserId(staff.getKeycloakUserId());
		    dto.setEnabled(staff.isEnabled());
		    if (staff.getServiceIds() != null) {
		        dto.setServiceIds(staff.getServiceIds());
		    }
		    return dto;	}

	private StaffResponseDTO mapToDto(UserRepresentation user) {
		StaffResponseDTO dto = new StaffResponseDTO();
		dto.setKeycloakUserId(user.getId());
		dto.setName(user.getFirstName());
		dto.setEmail(user.getEmail());
		dto.setPhone(user.getAttributes() != null && user.getAttributes().containsKey("phone")
				? user.getAttributes().get("phone").get(0)
				: null);
		dto.setEnabled(user.isEnabled());

		List<String> serviceIdsStr = CollectionUtils
				.isEmpty(user.getAttributes() != null ? user.getAttributes().get("serviceIds") : null) ? List.of()
						: user.getAttributes().get("serviceIds");
		dto.setServiceIds(serviceIdsStr);

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
		if (dto.getFirstName() != null)
			user.setFirstName(dto.getFirstName());
		if (dto.getLastName() != null)
			user.setLastName(dto.getLastName());
		if (dto.getEmailAddress() != null)
			user.setEmail(dto.getEmailAddress());

		Map<String, List<String>> attributes = user.getAttributes() != null ? user.getAttributes() : new HashMap<>();
		if (dto.getPhone() != null)
			attributes.put("phone", List.of(dto.getPhone()));
		if (!CollectionUtils.isEmpty(dto.getServiceIds()))
			attributes.put("serviceIds", dto.getServiceIds());
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

	private void validateServiceIds(List<String> serviceIds) {
		if (serviceIds == null || serviceIds.isEmpty())
			return;
	}
}
