package com.notification.service;

import org.springframework.stereotype.Service;

import com.notification.entity.NotificationRequest;
import com.notification.enums.NotificationStatus;
import com.notification.repository.NotificationRequestRepository;

@Service
public class SchedulingService {

	private final NotificationRequestRepository notificationRequestRepository;

	public SchedulingService(NotificationRequestRepository notificationRequestRepository) {
		this.notificationRequestRepository = notificationRequestRepository;
	}

	public void schedule(NotificationRequest request) {
		request.setStatus(NotificationStatus.PENDING);
		notificationRequestRepository.save(request);
	}
}
