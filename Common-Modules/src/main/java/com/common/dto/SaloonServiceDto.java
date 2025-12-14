package com.common.dto;

import java.math.BigDecimal;
import java.util.UUID;

import com.common.enums.ServiceCategory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaloonServiceDto {

	private UUID id;
    private String name;
    private String description;
    private ServiceCategory category;
    private Integer durationInMinutes;
    private BigDecimal price;
    private Boolean active;
    
    private UUID salonId;
    private String salonName;
}
