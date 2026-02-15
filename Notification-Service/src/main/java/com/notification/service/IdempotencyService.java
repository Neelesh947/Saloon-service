package com.notification.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.notification.entity.NotificationIdempotency;
import com.notification.repository.NotificationIdempotencyRepository;

@Service
public class IdempotencyService {

    private final NotificationIdempotencyRepository notificationIdempotencyRepository;
    
    public IdempotencyService(NotificationIdempotencyRepository idempotencyRepository) {
    	this.notificationIdempotencyRepository = idempotencyRepository;
    }

    public boolean isDuplicate(String eventId) {
        return notificationIdempotencyRepository.existsByEventId(eventId);
    }

    public void markProcessed(String eventId) {
        NotificationIdempotency record = new NotificationIdempotency();
        record.setEventId(eventId);
        record.setProcessed(true);
        record.setCreatedAt(LocalDateTime.now());
        notificationIdempotencyRepository.save(record);
    }
}

