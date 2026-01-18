package com.common.dto;

import java.math.BigDecimal;

import com.common.enums.ServiceCategory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateServiceDTO {

	private String name;
	private String description;
	private ServiceCategory category;
	private Integer durationInMinutes;
	private BigDecimal price;
	private Boolean active;
}