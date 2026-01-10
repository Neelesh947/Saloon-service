package com.keycloak.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ClientCredentialsDTO {

	private String clientId;

	private String clientSecret;

}
