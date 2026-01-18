package com.service.catalog.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.common.dto.CreateServiceDTO;
import com.common.dto.SaloonServiceDTO;
import com.common.dto.UpdateServiceDTO;
import com.common.utils.SecurityUtils;
import com.service.catalog.service.ServiceCatalogService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/{realm}/Saloons/SaloonService")
@RequiredArgsConstructor
public class SaloonServiceController {

	private final ServiceCatalogService serviceCatalogService;

	@PostMapping
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	public ResponseEntity<SaloonServiceDTO> createSaloonService(@RequestBody CreateServiceDTO createServiceDTO) {
		String saloonIdString = SecurityUtils.getCurrentUserIdSupplier.get();
		UUID saloonId = UUID.fromString(saloonIdString);
		SaloonServiceDTO createSerivce = serviceCatalogService.createService(createServiceDTO, saloonId);
		return ResponseEntity.ok(createSerivce);
	}

	@GetMapping
	public ResponseEntity<List<SaloonServiceDTO>> getAllServices() {
		return ResponseEntity.ok(serviceCatalogService.getAllServices());
	}

	@GetMapping("/{serviceId}")
	public ResponseEntity<SaloonServiceDTO> getServiceById(@PathVariable UUID serviceId) {
		return ResponseEntity.ok(serviceCatalogService.getServiceById(serviceId));
	}

	@GetMapping("/salon")
	public ResponseEntity<List<SaloonServiceDTO>> getServicesBySalon() {
		String salonIdsFormToken = SecurityUtils.getCurrentUserIdSupplier.get();
		UUID salonId = UUID.fromString(salonIdsFormToken);
		return ResponseEntity.ok(serviceCatalogService.getServicesBySalon(salonId));
	}

	@PutMapping("/{serviceId}")
	@PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
	public ResponseEntity<SaloonServiceDTO> updateService(@PathVariable UUID serviceId,
			@RequestBody UpdateServiceDTO updateServiceDTO) {
		SaloonServiceDTO updatedService = serviceCatalogService.updateService(serviceId, updateServiceDTO);
		return ResponseEntity.ok(updatedService);
	}

	@DeleteMapping("/{serviceId}")
	@PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
	public ResponseEntity<Void> deleteService(@PathVariable UUID serviceId) {
		serviceCatalogService.deleteService(serviceId);
		return ResponseEntity.noContent().build();
	}
}
