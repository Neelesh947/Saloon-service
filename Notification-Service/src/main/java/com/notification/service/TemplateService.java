package com.notification.service;

import org.springframework.stereotype.Service;

import com.notification.entity.NotificationTemplate;
import com.notification.entity.NotificationTemplateVersion;
import com.notification.enums.EventType;
import com.notification.enums.NotificationChannel;
import com.notification.repository.NotificationTemplateRepository;
import com.notification.repository.NotificationTemplateVersionRepository;

@Service
public class TemplateService {

	private final NotificationTemplateRepository templatNotificationTemplateRepository;
	private final NotificationTemplateVersionRepository versionNotificationTemplateVersionRepository;

	public TemplateService(NotificationTemplateRepository templatNotificationTemplateRepository,
			NotificationTemplateVersionRepository versionNotificationTemplateVersionRepository) {
		this.templatNotificationTemplateRepository = templatNotificationTemplateRepository;
		this.versionNotificationTemplateVersionRepository = versionNotificationTemplateVersionRepository;
	}

	public NotificationTemplateVersion getActiveTemplate(EventType eventType, NotificationChannel channelType,
			String tenantId) {

		NotificationTemplate template = templatNotificationTemplateRepository.findActiveTemplate(eventType, channelType, tenantId)
				.orElseThrow(() -> new RuntimeException("Template not found"));

		return versionNotificationTemplateVersionRepository.findTopByTemplateAndActiveTrueOrderByVersionNumberDesc(template)
				.orElseThrow(() -> new RuntimeException("Active template version not found"));
	}
}
