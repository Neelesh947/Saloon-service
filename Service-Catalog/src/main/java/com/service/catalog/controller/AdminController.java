package com.service.catalog.controller;

import java.util.List;
import java.util.Map;

import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.common.dto.AdminApprovalRequestDTO;
import com.common.dto.KeycloakuserDto;
import com.common.dto.PaginatedResponse;
import com.common.dto.SalonSignupRequestDTO;
import com.common.utils.SecurityUtils;
import com.service.catalog.service.AdminService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/{realm}/Admin")
@RequiredArgsConstructor
public class AdminController {

	// this class is admin means saloon and it is created on keycloak.

	private final AdminService adminService;

	@PostMapping("/create-request-for-admin")
	public ResponseEntity<?> createRequestToApproveAdmin(@RequestBody SalonSignupRequestDTO salonSignupRequestDTO,
			@PathVariable String realm) {
		adminService.createRequestToApproveAdmin(salonSignupRequestDTO, realm);
		return ResponseEntity.status(HttpStatus.CREATED).body("Salon request submitted for approval");
	}
	
	@PostMapping("/{requestId}/review")
	@PreAuthorize("hasAnyAuthority('SUPER_ADMIN')")
	public ResponseEntity<?> approveAdmin(@PathVariable String requestId, @RequestBody AdminApprovalRequestDTO dto, @PathVariable String realm) {
		String adminId = SecurityUtils.getCurrentUserIdSupplier.get();
		adminService.reviewAdminRequest(requestId, dto, realm, adminId);
        return ResponseEntity.ok("Admin request processed successfully");
	}
	
	/**
     * Get all pending salon/admin requests
     */
    @GetMapping("/pending/{status}")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<List<SalonSignupRequestDTO>> getPendingRequests(@PathVariable String status, @PathVariable String realm) {
        List<SalonSignupRequestDTO> pendingRequests = adminService.getPendingRequests(status, realm);
        return ResponseEntity.ok(pendingRequests);
    }

	@PostMapping("/createAdmin")
	@PreAuthorize("hasAnyAuthority('SUPER_ADMIN')")
	public ResponseEntity<Map<String, String>> createAdmin(@RequestBody KeycloakuserDto adminRequestDto,
			@PathVariable String realm) {
		String superAdminId = SecurityUtils.getCurrentUserIdSupplier.get();
		Map<String, String> response = adminService.createUser(adminRequestDto, superAdminId, realm);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@GetMapping("/{adminId}")
	@PreAuthorize("hasAuthority('SUPER_ADMIN')")
	public ResponseEntity<UserRepresentation> getAdminById(@PathVariable String adminId, @PathVariable String realm) {
		return ResponseEntity.ok(adminService.getAdminById(adminId, realm));
	}

	@GetMapping("/list")
	@PreAuthorize("hasAuthority('SUPER_ADMIN')")
	public ResponseEntity<PaginatedResponse<UserRepresentation>> getAllAdmins(@PathVariable String realm,
			@RequestParam Map<String, Object> allParams) {
		return ResponseEntity.ok(adminService.getAllAdmins(allParams, realm));
	}

	@PutMapping("/update/{adminId}")
	@PreAuthorize("hasAuthority('SUPER_ADMIN')")
	public ResponseEntity<Map<String, String>> updateAdmin(@PathVariable String adminId,
			@RequestBody KeycloakuserDto adminRequestDto, @PathVariable String realm) {
		String superAdminId = SecurityUtils.getCurrentUserIdSupplier.get();
		return ResponseEntity.ok(adminService.updateAdmin(adminId, adminRequestDto, superAdminId, realm));
	}

	@PutMapping("/status/{adminId}")
	@PreAuthorize("hasAuthority('SUPER_ADMIN')")
	public ResponseEntity<Map<String, String>> changeAdminStatus(@PathVariable String adminId,
			@RequestParam boolean enable, @PathVariable String realm) {
		return ResponseEntity.ok(adminService.changeAdminStatus(adminId, enable, realm));
	}

	@DeleteMapping("/{adminId}")
	@PreAuthorize("hasAuthority('SUPER_ADMIN')")
	public ResponseEntity<Map<String, String>> deleteAdmin(@PathVariable String adminId, @PathVariable String realm) {
		return ResponseEntity.ok(adminService.deleteAdmin(adminId, realm));
	}
}