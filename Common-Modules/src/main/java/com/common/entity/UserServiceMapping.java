package com.common.entity;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user_service_mapping")
@Getter
@Setter
public class UserServiceMapping extends BaseEntity {

	@Column(nullable = false)
	private UUID linkedUserId;

	@Column(nullable = false)
	private UUID linkedServiceId;
	
	@Column(nullable = false)
    private String status; // BOOKED / CANCELLED / COMPLETED
}
