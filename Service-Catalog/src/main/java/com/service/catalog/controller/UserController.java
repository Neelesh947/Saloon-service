package com.service.catalog.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.common.dto.PaginatedResponse;
import com.common.dto.UserRequestDTO;
import com.common.dto.UserResponseDTO;
import com.service.catalog.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/{realm}/User")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@PostMapping("/create")
	public ResponseEntity<Map<String, String>> createUser(@RequestBody UserRequestDTO dto, @PathVariable String realm) {
		return ResponseEntity.ok(userService.createUser(dto, realm));
	}

	@GetMapping("/list")
	@PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN')")
	public ResponseEntity<PaginatedResponse<UserResponseDTO>> getAllUsers(@RequestParam Map<String, Object> allParams,
			@PathVariable String realm) {
		return ResponseEntity.ok(userService.getAllUsers(allParams, realm));
	}

	@GetMapping("/by-id/{id}")
	@PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN')")
	public ResponseEntity<UserResponseDTO> getUserById(@PathVariable String id, @PathVariable String realm) {
		return ResponseEntity.ok(userService.getUserById(id, realm));
	}
}
