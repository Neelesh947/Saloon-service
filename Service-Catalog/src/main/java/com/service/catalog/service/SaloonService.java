package com.service.catalog.service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.common.dto.PaginatedResponse;
import com.common.dto.SalonResponseDTO;
import com.common.dto.SaloonRequestDTO;
import com.common.entity.Salon;
import com.exception.handling.models.ValidationException;
import com.service.catalog.repository.SaloonRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SaloonService {

	private final SaloonRepository saloonRepository;

	public SalonResponseDTO createSalonByAdmin(SaloonRequestDTO saloonRequestDTO, String adminId, String realm) {
		if (saloonRepository.existsBySaloonName(saloonRequestDTO.getSaloonName())) {
			throw new ValidationException("Salon already exists with name: " + saloonRequestDTO.getSaloonName());
		}
		Salon saloon = new Salon();
		saloon.setActive(true);
		saloon.setAddress(saloonRequestDTO.getAddress());
		saloon.setCreatedBy(adminId);
		saloon.setPhone(saloonRequestDTO.getPhone());
		saloon.setSaloonName(saloonRequestDTO.getSaloonName());
		Salon savedSalon = saloonRepository.save(saloon);
		return mapToDto(savedSalon);
	}

	private SalonResponseDTO mapToDto(Salon salon) {
		SalonResponseDTO dto = new SalonResponseDTO();
		dto.setId(salon.getId());
		dto.setSaloonName(salon.getSaloonName());
		dto.setAddress(salon.getAddress());
		dto.setPhone(salon.getPhone());
		dto.setActive(salon.getActive());
		dto.setCreatedAt(salon.getCreatedAt());
		dto.setUpdatedAt(salon.getUpdatedAt());
		dto.setServices(List.of());
		dto.setCreatedBy(salon.getCreatedBy());
		return dto;
	}

	public PaginatedResponse<SalonResponseDTO> getAllSalons(Map<String, Object> allParams) {
		Object isEnabledObj = allParams.get("isEnabled");
		String isEnabled = (isEnabledObj != null) ? isEnabledObj.toString().toUpperCase() : "all";
		int page = allParams.containsKey("page") ? Integer.parseInt(allParams.get("page").toString()) : 0;
		int size = allParams.containsKey("size") ? Integer.parseInt(allParams.get("size").toString()) : 10;
		PageRequest pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
		Page<Salon> salonPage;
		switch (isEnabled) {
		case "TRUE":
			salonPage = saloonRepository.findByActive(true, pageable);
			break;
		case "FALSE":
			salonPage = saloonRepository.findByActive(false, pageable);
			break;
		default: // "all"
			salonPage = saloonRepository.findAll(pageable);
			break;
		}

		List<SalonResponseDTO> listOFSaloon = salonPage.stream().map(this::mapToDto).collect(Collectors.toList());
		return new PaginatedResponse<>(listOFSaloon, listOFSaloon.size(), salonPage.getNumber(), salonPage.getSize());
	}

	public void updateStatus(Map<String, Object> allParams, UUID id) {
		Object isEnabledObj = allParams.get("isEnabled");
		if (isEnabledObj == null) {
			throw new ValidationException("'isEnabled' parameter is required");
		}

		boolean isEnabled;
		try {
			isEnabled = Boolean.parseBoolean(isEnabledObj.toString());
		} catch (Exception e) {
			throw new ValidationException("'isEnabled' parameter must be true or false");
		}

		Salon salon = saloonRepository.findById(id)
				.orElseThrow(() -> new ValidationException("Salon not found with ID: " + id));

		salon.setActive(isEnabled);
		saloonRepository.save(salon);
	}

	public SalonResponseDTO getSaloonById(UUID id) {
		Salon saloon = saloonRepository.findById(id)
				.orElseThrow(() -> new ValidationException("Salon not found with ID: " + id));
		return mapToDto(saloon);
	}

	public SalonResponseDTO updateSaloon(UUID saloonId, SaloonRequestDTO saloonRequestDTO) {
		Salon saloon = saloonRepository.findById(saloonId)
				.orElseThrow(() -> new ValidationException("Salon not found with ID: " + saloonId));
		if (saloonRequestDTO.getSaloonName() != null)
			saloon.setSaloonName(saloonRequestDTO.getSaloonName());
		if (saloonRequestDTO.getAddress() != null)
			saloon.setAddress(saloonRequestDTO.getAddress());
		if (saloonRequestDTO.getPhone() != null)
			saloon.setPhone(saloonRequestDTO.getPhone());
		saloon.setActive(saloonRequestDTO.getActive());
		Salon updatedSaloon = saloonRepository.save(saloon);
		return mapToDto(updatedSaloon);
	}

	public void deleteSalon(UUID id) {
		Salon saloon = saloonRepository.findById(id)
				.orElseThrow(() -> new ValidationException("Salon not found with ID: " + id));
		saloonRepository.delete(saloon);
	}
}
