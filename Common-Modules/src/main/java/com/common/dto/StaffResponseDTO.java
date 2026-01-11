package com.common.dto;

import java.util.List;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StaffResponseDTO extends BaseResponseDTO {
	private String name;
	private String email;
	private String phone;
	private String keycloakUserId;
	/**
	 * List of service IDs assigned to this staff
	 */
	private List<UUID> serviceIds;
}
