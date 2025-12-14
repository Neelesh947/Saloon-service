package com.service.catalog.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.common.dto.CreateServiceDTO;
import com.common.dto.SaloonServiceDto;
import com.common.dto.UpdateServiceDTO;
import com.common.utils.SecurityUtils;
import com.service.catalog.service.ServiceCatalogService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/{realm}/SaloonService")
@RequiredArgsConstructor
public class SaloonServiceController {

	private final ServiceCatalogService serviceCatalogService;

	@PostMapping
	public ResponseEntity<SaloonServiceDto> createSaloonService(@RequestBody CreateServiceDTO createServiceDTO) {
		String saloonIdString = SecurityUtils.getCurrentUserIdSupplier.get();
		UUID saloonId = UUID.fromString(saloonIdString);
		SaloonServiceDto createSerivce = serviceCatalogService.createService(createServiceDTO, saloonId);
		return ResponseEntity.ok(createSerivce);
	}

	@GetMapping
	public ResponseEntity<List<SaloonServiceDto>> getAllServices() {
		return ResponseEntity.ok(serviceCatalogService.getAllServices());
	}

	@GetMapping("/{serviceId}")
	public ResponseEntity<SaloonServiceDto> getServiceById(@PathVariable UUID serviceId) {
		return ResponseEntity.ok(serviceCatalogService.getServiceById(serviceId));
	}

	@GetMapping("/salon/{salonId}")
	public ResponseEntity<List<SaloonServiceDto>> getServicesBySalon(@PathVariable UUID salonId) {
		return ResponseEntity.ok(serviceCatalogService.getServicesBySalon(salonId));
	}

	@PutMapping("/{serviceId}")
	public ResponseEntity<SaloonServiceDto> updateService(@PathVariable UUID serviceId,
			@RequestBody UpdateServiceDTO updateServiceDTO) {
		SaloonServiceDto updatedService = serviceCatalogService.updateService(serviceId, updateServiceDTO);
		return ResponseEntity.ok(updatedService);
	}
	
	@DeleteMapping("/{serviceId}")
    public ResponseEntity<Void> deleteService(@PathVariable UUID serviceId) {
		serviceCatalogService.deleteService(serviceId);
        return ResponseEntity.noContent().build();
    }
}
