package com.common.dto;

import java.util.List;
import java.util.Map;

import com.common.entity.SaloonService;

import lombok.Data;

@Data
public class UserResponseDTO {

	private String keycloakUserId;
	private String firstName;
	private String lastName;
	private String email;
	private boolean enable;
	private Map<String, List<String>> attributes;
	private List<SaloonService> bookedServiceIds;
}
