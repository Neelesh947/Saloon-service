package com.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorResponseDto {

	private String error;

	private String errorMessage;

	@JsonProperty("error_description")
	private String errorDescription;
}
