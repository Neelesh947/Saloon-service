package com.notification.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.notification.entity.NotificationRequest;
import com.notification.entity.NotificationRetry;
import com.notification.repository.NotificationRetryRepository;

@Service
public class RetryService {

	private final NotificationRetryRepository notificationRetryRepository;

	public RetryService(NotificationRetryRepository notificationRetryRepository) {
		this.notificationRetryRepository = notificationRetryRepository;
	}

	private static final int MAX_RETRY = 3;

	public void scheduleRetry(NotificationRequest request) {
		if (request.getRetryCount() >= MAX_RETRY) {
			return;
		}
		NotificationRetry retry = new NotificationRetry();
		retry.setRequest(request);
		retry.setRetryCount(request.getRetryCount() + 1);
		retry.setNextRetryAt(LocalDateTime.now().plusMinutes((long) Math.pow(2, request.getRetryCount())));
		retry.setExhausted(false);
		notificationRetryRepository.save(retry);
	}
}
