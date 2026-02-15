package com.common.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StaffResponseDTO extends BaseResponseDTO {
	private String name;
	private String email;
	private String phone;
	private String keycloakUserId;
	private boolean enabled;
	/**
	 * List of service IDs assigned to this staff
	 */
	private List<String> serviceIds;
}
