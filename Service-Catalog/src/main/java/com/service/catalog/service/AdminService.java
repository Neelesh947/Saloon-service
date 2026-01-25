package com.service.catalog.service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import com.common.dto.AdminApprovalRequestDTO;
import com.common.dto.KeycloakuserDto;
import com.common.dto.PaginatedResponse;
import com.common.dto.SalonSignupRequestDTO;
import com.common.entity.Salon;
import com.common.entity.SalonSignupRequest;
import com.common.enums.RequestStatus;
import com.common.utils.KeycloakUtility;
import com.exception.handling.models.ValidationException;
import com.service.catalog.repository.SalonSignupRequestRepository;
import com.service.catalog.repository.SaloonRepository;

@Service
public class AdminService {

	private static final String ADMIN_ROLE = "ADMIN";

	private KeycloakUtility keycloakUtility;

	private SalonSignupRequestRepository salonSignupRequestRepository;
	private SaloonRepository saloonRepository;

	public AdminService(KeycloakUtility keycloakUtility, SalonSignupRequestRepository salonSignupRequestRepository,
			SaloonRepository saloonRepository) {
		this.keycloakUtility = keycloakUtility;
		this.salonSignupRequestRepository = salonSignupRequestRepository;
		this.saloonRepository = saloonRepository;
	}

	public Map<String, String> createUser(KeycloakuserDto adminRequestDto, String superAdminId, String realm) {
		if (ObjectUtils.isEmpty(adminRequestDto.getUsername())) {
			throw new ValidationException("Username is required");
		}
		if (ObjectUtils.isEmpty(adminRequestDto.getEmailAddress())) {
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
		List<UserRepresentation> usersByEmail = keycloakUtility.userByEmailAndRole(adminRequestDto.getEmailAddress(),
				ADMIN_ROLE, realm);
		if (!CollectionUtils.isEmpty(usersByEmail)) {
			throw new ValidationException("Email already exists for role " + ADMIN_ROLE);
		}
		List<UserRepresentation> usersByPhone = keycloakUtility.userByPhoneAndRole(phoneNumber, ADMIN_ROLE, realm);
		if (!CollectionUtils.isEmpty(usersByPhone)) {
			throw new ValidationException("Phone number already exists for role " + ADMIN_ROLE);
		}
		adminRequestDto.setCreatedBy(superAdminId);
		return keycloakUtility.createUser(adminRequestDto, ADMIN_ROLE, realm);
	}

	/**
	 * Extracts phone number from Keycloak attributes
	 */
	private String extractPhoneNumber(KeycloakuserDto dto) {
		if (ObjectUtils.isEmpty(dto.getPhoneNumber())) {
			return null;
		}
		return dto.getPhoneNumber();
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

	public Map<String, String> updateAdmin(String adminId, KeycloakuserDto dto, String superAdminId, String realm) {
		UserRepresentation existingUser = getAdminById(adminId, realm);
		mapDtoToUser(existingUser, dto);
		keycloakUtility.updateUser(existingUser, adminId, realm);
		return Map.of("status", "success", "message", "Admin updated successfully");
	}

	private void mapDtoToUser(UserRepresentation user, KeycloakuserDto dto) {
		if (dto.getFirstName() != null)
			user.setFirstName(dto.getFirstName());
		if (dto.getLastName() != null)
			user.setLastName(dto.getLastName());
		if (dto.getEmailAddress() != null)
			user.setEmail(dto.getEmailAddress());
		if (dto.getUsername() != null)
			user.setUsername(dto.getUsername());
	}

	public Map<String, String> changeAdminStatus(String adminId, boolean enable, String realm) {
		UserRepresentation user = getAdminById(adminId, realm);
		user.setEnabled(enable);
		keycloakUtility.updateUser(user, adminId, realm);
		return Map.of("status", "success", "message", enable ? "Admin enabled" : "Admin disabled");
	}

	public Map<String, String> deleteAdmin(String adminId, String realm) {
		UserRepresentation user = getAdminById(adminId, realm);
		user.setEnabled(false);
		keycloakUtility.updateUser(user, adminId, realm);
		return Map.of("status", "success", "message", "Admin deleted successfully");
	}

	public void createRequestToApproveAdmin(SalonSignupRequestDTO dto, String realm) {
		boolean pendingEmail = salonSignupRequestRepository.existsByEmailAndStatus(dto.getEmail(),
				RequestStatus.PENDING);
		boolean pendingMobile = salonSignupRequestRepository.existsByMobileAndStatus(dto.getMobile(),
				RequestStatus.PENDING);

		if (pendingEmail || pendingMobile) {
			if (pendingEmail && pendingMobile) {
				throw new ValidationException(
						"A signup request is already pending for this email address and mobile number.");
			} else if (pendingEmail) {
				throw new ValidationException("A signup request is already pending for this email address.");
			} else {
				throw new ValidationException("A signup request is already pending for this mobile number.");
			}
		}

		boolean approvedEmail = salonSignupRequestRepository.existsByEmailAndStatus(dto.getEmail(),
				RequestStatus.APPROVED);
		boolean approvedMobile = salonSignupRequestRepository.existsByMobileAndStatus(dto.getMobile(),
				RequestStatus.APPROVED);

		if (approvedEmail || approvedMobile) {
			if (approvedEmail && approvedMobile) {
				throw new ValidationException("An account already exists with this email address and mobile number.");
			} else if (approvedEmail) {
				throw new ValidationException("An account already exists with this email address.");
			} else {
				throw new ValidationException("An account already exists with this mobile number.");
			}
		}

		SalonSignupRequest request = new SalonSignupRequest();
		request.setOwnerName(dto.getOwnerName());
		request.setEmail(dto.getEmail());
		request.setMobile(dto.getMobile());
		request.setSalonName(dto.getSalonName());
		request.setStatus(RequestStatus.PENDING);

		salonSignupRequestRepository.save(request);
	}

	public void reviewAdminRequest(String requestId, AdminApprovalRequestDTO dto, String realm, String superAdminId) {
		SalonSignupRequest request = salonSignupRequestRepository.findById(UUID.fromString(requestId))
				.orElseThrow(() -> new ValidationException("Request not found"));
		if (request.getStatus() != RequestStatus.PENDING) {
			throw new ValidationException("Request already reviewed");
		}
		if (Boolean.TRUE.equals(dto.getApproved())) {
			approveRequest(request, realm, superAdminId, dto.getRemarks());
		} else {
			rejectRequest(request, dto.getRemarks(), superAdminId);
		}
	}

	@Transactional
	private void rejectRequest(SalonSignupRequest request, String remarks, String superAdminId) {
		if (remarks == null || remarks.isBlank()) {
			throw new ValidationException("Remarks are required for rejection");
		}
		request.setStatus(RequestStatus.REJECTED);
		request.setRemarks(request.getRemarks());
		request.setReviewedBy(superAdminId);
		request.setReviewedAt(LocalDateTime.now());
		salonSignupRequestRepository.save(request);
	}

	@Transactional
	private void approveRequest(SalonSignupRequest request, String realm, String superAdminId, String remarks) {
		KeycloakuserDto keycloakDto = new KeycloakuserDto();
		keycloakDto.setUsername(request.getOwnerName().toLowerCase().replaceAll("\\s+", "_"));
		keycloakDto.setEmailAddress(request.getEmail());
		keycloakDto.setPhoneNumber(request.getMobile());
		String firstName = "";
		String lastName = "";
		if (request.getOwnerName() != null && !request.getOwnerName().isBlank()) {
			String[] parts = request.getOwnerName().trim().split("\\s+");
			firstName = parts[0];
			if (parts.length > 1) {
				lastName = String.join(" ", Arrays.copyOfRange(parts, 1, parts.length));
			}
		}
		keycloakDto.setFirstName(firstName);
		keycloakDto.setLastName(lastName);
		Map<String, String> createdUser = createUser(keycloakDto, superAdminId, realm);

		Salon salon = new Salon();
		salon.setSaloonName(request.getSalonName());
		salon.setCreatedBy(createdUser.get("userId"));
		salon.setActive(true);
		salon.setPhone(keycloakDto.getPhoneNumber());
		saloonRepository.save(salon);

		request.setStatus(RequestStatus.APPROVED);
		request.setReviewedBy(superAdminId);
		request.setRealm(realm);
		request.setRemarks(remarks);
		request.setReviewedAt(LocalDateTime.now());
		salonSignupRequestRepository.save(request);
	}

	public List<SalonSignupRequestDTO> getPendingRequests(String status, String realm) {
		RequestStatus requestStatus;
		try {
			requestStatus = RequestStatus.valueOf(status.toUpperCase());
		} catch (IllegalArgumentException e) {
			throw new ValidationException("Invalid request status: " + status);
		}
		List<SalonSignupRequest> pendingRequests = salonSignupRequestRepository.findByStatus(requestStatus);
		return pendingRequests.stream().map(this::mapToDTO).collect(Collectors.toList());
	}

	/**
	 * Map entity to DTO
	 */
	private SalonSignupRequestDTO mapToDTO(SalonSignupRequest request) {
		SalonSignupRequestDTO dto = new SalonSignupRequestDTO();
		dto.setId(request.getId().toString());
		dto.setOwnerName(request.getOwnerName());
		dto.setEmail(request.getEmail());
		dto.setMobile(request.getMobile());
		dto.setSalonName(request.getSalonName());
		dto.setStatus(request.getStatus().name());
		dto.setRealm(request.getRealm());
		return dto;
	}
}
