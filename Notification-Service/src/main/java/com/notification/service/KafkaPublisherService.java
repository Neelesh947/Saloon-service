package com.notification.service;

import org.springframework.stereotype.Component;

import com.notification.entity.NotificationRequest;

@Component
public class KafkaPublisherService {

	private final NotificationOrchestratorService orchestrator;

	public KafkaPublisherService(NotificationOrchestratorService orchestrator) {
		this.orchestrator = orchestrator;
	}
	
//	@KafkaListener(topics = "notification-topic")
    public void consume(NotificationRequest request) {
        orchestrator.process(request);
    }
}
