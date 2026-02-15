package com.notification.service;

import org.springframework.stereotype.Service;

import com.notification.entity.NotificationPreference;
import com.notification.entity.NotificationRequest;
import com.notification.repository.NotificationPreferenceRepository;

@Service
public class PreferenceService {

	private final NotificationPreferenceRepository notificationPreferenceRepository;
	
	public PreferenceService(NotificationPreferenceRepository notificationPreferenceRepository) {
		this.notificationPreferenceRepository = notificationPreferenceRepository;
	}

	public boolean isAllowed(NotificationRequest request) {
		return notificationPreferenceRepository.findByUserIdAndChannelTypeAndEventType(request.getUserId(), request.getChannelType(),
				request.getEventType()).map(NotificationPreference::getEnabled).orElse(true);
	}
}
