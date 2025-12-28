package com.service.catalog.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.common.dto.CreateServiceDTO;
import com.common.dto.SaloonServiceDTO;
import com.common.dto.UpdateServiceDTO;
import com.common.entity.Salon;
import com.common.entity.SaloonService;
import com.exception.handling.models.ValidationException;
import com.service.catalog.repository.SaloonRepository;
import com.service.catalog.repository.SaloonServiceRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ServiceCatalogService {

	private final SaloonServiceRepository saloonServiceRepository;
	private final SaloonRepository saloonRepository;

	public SaloonServiceDTO createService(CreateServiceDTO createServiceDTO, UUID salonId) {
		Salon salon = saloonRepository.findById(salonId).orElseThrow(() -> new ValidationException("Salon not found"));
		SaloonService service = new SaloonService();
		service.setName(createServiceDTO.getName());
		service.setDescription(createServiceDTO.getDescription());
		service.setCategory(createServiceDTO.getCategory());
		service.setDurationInMinutes(createServiceDTO.getDurationInMinutes());
		service.setPrice(createServiceDTO.getPrice());
		service.setActive(createServiceDTO.getActive());
		service.setSalon(salon);
		return convertToDto(saloonServiceRepository.save(service));
	}

	public List<SaloonServiceDTO> getAllServices() {
		return saloonServiceRepository.findAll().stream().map(this::convertToDto).collect(Collectors.toList());
	}

	public SaloonServiceDTO getServiceById(UUID serviceId) {
		SaloonService service = saloonServiceRepository.findById(serviceId)
				.orElseThrow(() -> new ValidationException("Service not found"));
		return convertToDto(service);
	}

	public List<SaloonServiceDTO> getServicesBySalon(UUID salonId) {
		Salon salon = saloonRepository.findById(salonId).orElseThrow(() -> new ValidationException("Salon not found"));
		return salon.getServices().stream().map(this::convertToDto).collect(Collectors.toList());
	}

	public SaloonServiceDTO updateService(UUID serviceId, UpdateServiceDTO updateDTO) {
		SaloonService service = saloonServiceRepository.findById(serviceId)
				.orElseThrow(() -> new ValidationException("Service not found"));

		if (updateDTO.getName() != null)
			service.setName(updateDTO.getName());
		if (updateDTO.getDescription() != null)
			service.setDescription(updateDTO.getDescription());
		if (updateDTO.getCategory() != null)
			service.setCategory(updateDTO.getCategory());
		if (updateDTO.getDurationInMinutes() != null)
			service.setDurationInMinutes(updateDTO.getDurationInMinutes());
		if (updateDTO.getPrice() != null)
			service.setPrice(updateDTO.getPrice());
		if (updateDTO.getActive() != null)
			service.setActive(updateDTO.getActive());

		if (updateDTO.getSalonId() != null) {
			Salon salon = saloonRepository.findById(updateDTO.getSalonId())
					.orElseThrow(() -> new ValidationException("Salon not found"));
			service.setSalon(salon);
		}

		return convertToDto(saloonServiceRepository.save(service));
	}

	public void deleteService(UUID serviceId) {
		SaloonService service = saloonServiceRepository.findById(serviceId)
				.orElseThrow(() -> new ValidationException("Service not found"));
		saloonServiceRepository.delete(service);
	}

	private SaloonServiceDTO convertToDto(SaloonService saloonService) {
		return new SaloonServiceDTO(saloonService.getId(), saloonService.getName(), saloonService.getDescription(),
				saloonService.getCategory(), saloonService.getDurationInMinutes(), saloonService.getPrice(),
				saloonService.getActive(), saloonService.getSalon() != null ? saloonService.getSalon().getId() : null,
				saloonService.getSalon() != null ? saloonService.getSalon().getSaloonName() : null);
	}
}
