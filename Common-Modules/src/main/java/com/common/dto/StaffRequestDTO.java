package com.common.dto;

import java.util.List;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StaffRequestDTO {
	private String name;
	private String email;
	private String phone;
	private String keycloakUserId;
	private List<UUID> serviceIds;
}
