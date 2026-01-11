package com.service.catalog.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.common.dto.PaginatedResponse;
import com.common.dto.StaffRequestDTO;
import com.common.dto.StaffResponseDTO;
import com.common.utils.SecurityUtils;
import com.service.catalog.service.StaffService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/{realm}/Staff")
@RequiredArgsConstructor
public class StaffController {

	private final StaffService staffService;

	@PostMapping
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Map<String, String>> createStaff(@PathVariable String realm,
			@Valid @RequestBody StaffRequestDTO staffDto) {
		String adminId = SecurityUtils.getCurrentUserIdSupplier.get();
		Map<String, String> response = staffService.createStaff(staffDto, adminId, realm);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/list")
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	public ResponseEntity<PaginatedResponse<StaffResponseDTO>> getListOfStaff(
			@RequestParam Map<String, Object> allParams, @PathVariable String realm) {
		String adminId = SecurityUtils.getCurrentUserIdSupplier.get();
		PaginatedResponse<StaffResponseDTO> salons = staffService.getAllStaff(allParams, adminId, realm);
		return ResponseEntity.ok(salons);
	}

	@GetMapping("/{staffId}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<StaffResponseDTO> getStaffById(@PathVariable String staffId, @PathVariable String realm) {
		return ResponseEntity.ok(staffService.getStaffById(staffId, realm));
	}

	@PutMapping("/{staffId}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<StaffResponseDTO> updateStaff(@PathVariable String staffId, @RequestBody StaffRequestDTO dto,
			@PathVariable String realm) {
		return ResponseEntity.ok(staffService.updateStaff(staffId, dto, realm));
	}

	@PatchMapping("/status/{staffId}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Map<String, String>> changeStaffStatus(@PathVariable String staffId,
			@RequestParam boolean enable, @PathVariable String realm) {
		staffService.changeStaffStatus(staffId, enable, realm);
		return ResponseEntity.ok(Map.of("status", "success", "message", enable ? "Staff enabled" : "Staff disabled"));
	}
}
