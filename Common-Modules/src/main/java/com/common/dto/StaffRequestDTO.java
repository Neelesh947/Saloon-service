package com.common.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StaffRequestDTO {
	private String username;
	private String firstName;
	private String lastName;
	private boolean enable;
	private String emailAddress;
	private String phone;
	/**
     * List of services assigned to this staff.
     * These must exist in the service table in the DB.
     */
	private List<String> serviceIds;
}
