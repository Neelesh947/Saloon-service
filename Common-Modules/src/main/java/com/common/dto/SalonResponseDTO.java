package com.common.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SalonResponseDTO extends BaseResponseDTO {
	private String saloonName;
	private String address;
	private String phone;
	private Boolean active;
	private List<SaloonServiceDTO> services;
	private String createdBy;
}
