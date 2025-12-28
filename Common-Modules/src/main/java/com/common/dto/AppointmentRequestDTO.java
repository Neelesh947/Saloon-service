package com.common.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppointmentRequestDTO {
	private String keycloakUserId;
	private UUID serviceId;
	private UUID staffId;
	private LocalDateTime appointmentTime;
}
