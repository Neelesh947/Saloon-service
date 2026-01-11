package com.service.catalog.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.common.dto.UserRequestDTO;
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
}
