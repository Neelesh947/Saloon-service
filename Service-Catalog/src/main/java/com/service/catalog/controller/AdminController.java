package com.service.catalog.controller;

import java.util.Map;

import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.common.dto.KeycloakuserDto;
import com.common.dto.PaginatedResponse;
import com.common.utils.SecurityUtils;
import com.service.catalog.service.AdminService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/{realm}/Admin")
@RequiredArgsConstructor
public class AdminController {

	private final AdminService adminService;

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

}