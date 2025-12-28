package com.common.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.common.enums.AppointmentStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppointmentResponseDTO extends BaseResponseDTO {
	private String keycloakUserId;
	private UUID serviceId;
	private UUID staffId;
	private LocalDateTime appointmentTime;
	private AppointmentStatus status;
}