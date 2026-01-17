package com.common.entity;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", nullable = false)
	private SaloonService linkedServiceId;
	
	@Column(nullable = false)
    private String status; // BOOKED / CANCELLED / COMPLETED
}
