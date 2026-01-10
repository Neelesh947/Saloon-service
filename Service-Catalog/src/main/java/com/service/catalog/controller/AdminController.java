package com.service.catalog.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.common.dto.KeycloakuserDto;
import com.common.utils.KeycloakUtility;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/{realm}/Admin")
@RequiredArgsConstructor
public class AdminController {

	private final KeycloakUtility keycloakUtility;

	@PostMapping("/createAdmin")
	public ResponseEntity<Map<String, String>> createAdmin(@RequestBody KeycloakuserDto adminRequestDto,
			@PathVariable String realm) {
		Map<String, String> response = keycloakUtility.createUser(adminRequestDto, "admin", realm);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

}