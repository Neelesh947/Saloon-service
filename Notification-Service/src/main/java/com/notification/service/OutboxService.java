package com.notification.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.notification.entity.NotificationOutbox;
import com.notification.repository.NotificationOutboxRepository;

@Service
public class OutboxService {

	private final NotificationOutboxRepository notificationOutboxRepository;

	public OutboxService(NotificationOutboxRepository notificationOutboxRepository) {
		this.notificationOutboxRepository = notificationOutboxRepository;
	}

	public void saveEvent(String aggregateType, String aggregateId, String payload) {
		NotificationOutbox outbox = new NotificationOutbox();
		outbox.setAggregateType(aggregateType);
		outbox.setAggregateId(aggregateId);
		outbox.setPayload(payload);
		outbox.setProcessed(false);
		outbox.setCreatedAt(LocalDateTime.now());
		notificationOutboxRepository.save(outbox);
	}
}
