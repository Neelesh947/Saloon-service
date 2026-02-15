package com.notification.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.notification.entity.NotificationRequest;
import com.notification.repository.NotificationRequestRepository;
import com.notification.service.NotificationOrchestratorService;

@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {

	private final NotificationOrchestratorService orchestrator;
	private final NotificationRequestRepository repository;

	public NotificationController(NotificationOrchestratorService orchestrator,
			NotificationRequestRepository repository) {
		this.orchestrator = orchestrator;
		this.repository = repository;
	}

	@PostMapping
	public ResponseEntity<String> send(@RequestBody NotificationRequest request) {
		repository.save(request);
		orchestrator.process(request);
		return ResponseEntity.ok("Notification processing started");
	}
}
