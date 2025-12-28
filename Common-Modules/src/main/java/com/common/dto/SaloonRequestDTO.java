package com.common.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaloonRequestDTO {

	@NotBlank(message = "Saloon name is required")
	@Size(min = 3, max = 100, message = "Saloon name must be between 3 and 100 characters")
	private String saloonName;

	@NotBlank(message = "Address is required")
	@Size(max = 255, message = "Address cannot exceed 255 characters")
	private String address;

	@NotBlank(message = "Phone number is required")
	@Size(min = 8, max = 15, message = "Phone number must be valid")
	private String phone;

	@NotNull(message = "Active flag must be provided")
	private Boolean active;
}
