package com.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_service_mapping")
public class UserServiceMapping extends BaseEntity {

	@Column(nullable = false)
	private String linkedUserId;

	@Column(nullable = false)
	private String linkedServiceId;
	
	@Column(nullable = false)
    private String status;
}
