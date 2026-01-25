package com.common.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SalonSignupRequestDTO {
	
	private String id;

	@NotBlank
	private String ownerName;

	@Email
	@NotBlank
	private String email;

	@NotBlank
	private String mobile;

	@NotBlank
	private String salonName;
	
	private String status;
    private String realm;
}
