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
	/**
     * List of services assigned to this staff.
     * These must exist in the service table in the DB.
     */
	private List<UUID> serviceIds;
}
