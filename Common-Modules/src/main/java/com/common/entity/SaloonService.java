package com.common.entity;

import java.math.BigDecimal;

import com.common.enums.ServiceCategory;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "services")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SaloonService extends BaseEntity {

	@Column(nullable = false, unique = true)
	private String name;

	@Column(length = 500)
	private String description;

	@Enumerated(EnumType.STRING)
	private ServiceCategory category;

	@Column(nullable = false)
	private Integer durationInMinutes;

	@Column(nullable = false)
	private BigDecimal price;

	private Boolean active;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "salon_id")
	private Salon salon;
}