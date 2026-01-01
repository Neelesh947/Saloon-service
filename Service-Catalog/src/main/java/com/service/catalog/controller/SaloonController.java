package com.service.catalog.controller;

import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.common.dto.PaginatedResponse;
import com.common.dto.SalonResponseDTO;
import com.common.dto.SaloonRequestDTO;
import com.common.utils.SecurityUtils;
import com.service.catalog.service.SaloonService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/{realm}/Saloons")
@RequiredArgsConstructor
public class SaloonController {

	private final SaloonService saloonService;

	@PostMapping("/create")
	@PreAuthorize("hasAnyAuthority('SUPER_ADMIN')")
	public ResponseEntity<SalonResponseDTO> createSaloonByAdmin(@RequestBody SaloonRequestDTO saloonRequestDTO,
			@PathVariable String realm) {
		String adminId = SecurityUtils.getCurrentUserIdSupplier.get();
		SalonResponseDTO response = saloonService.createSalonByAdmin(saloonRequestDTO, adminId, realm);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@GetMapping("/list")
	public ResponseEntity<PaginatedResponse<SalonResponseDTO>> getListOfSaloon(
			@RequestParam Map<String, Object> allParams) {
		PaginatedResponse<SalonResponseDTO> salons = saloonService.getAllSalons(allParams);
		return ResponseEntity.ok(salons);
	}

	@PatchMapping("/update-status/{id}")
	@PreAuthorize("hasAnyAuthority('SUPER_ADMIN')")
	public void updateSaloonStatus(@RequestParam Map<String, Object> allParams, @PathVariable UUID id) {
		saloonService.updateStatus(allParams, id);
	}

	@GetMapping("/{id}")
	public ResponseEntity<SalonResponseDTO> getSaloonById(@PathVariable UUID id) {
		SalonResponseDTO response = saloonService.getSaloonById(id);
		return ResponseEntity.ok(response);
	}

	@PutMapping("/{saloonId}")
	@PreAuthorize("hasAnyAuthority('SUPER_ADMIN')")
	public ResponseEntity<SalonResponseDTO> updateSaloon(@PathVariable UUID saloonId,
			@RequestBody SaloonRequestDTO saloonRequestDTO) {
		SalonResponseDTO response = saloonService.updateSaloon(saloonId, saloonRequestDTO);
		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasAnyAuthority('SUPER_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSalon(@PathVariable UUID id) {
        saloonService.deleteSalon(id);
    }
}
