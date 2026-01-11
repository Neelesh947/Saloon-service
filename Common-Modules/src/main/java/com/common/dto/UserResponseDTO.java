package com.common.dto;

import java.util.List;
import java.util.UUID;

import lombok.Data;

@Data
public class UserResponseDTO {

	private String keycloakUserId;
	private String firstName;
	private String lastName;
	private String email;
	private String phone;
	private List<UUID> bookedServiceIds;
}
