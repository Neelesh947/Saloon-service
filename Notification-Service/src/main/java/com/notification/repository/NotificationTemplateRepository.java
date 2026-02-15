package com.notification.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.notification.entity.NotificationTemplate;
import com.notification.enums.EventType;
import com.notification.enums.NotificationChannel;

@Repository
public interface NotificationTemplateRepository extends JpaRepository<NotificationTemplate, UUID> {

	Optional<NotificationTemplate> findActiveTemplate(EventType eventType, NotificationChannel channelType, String tenantId);

}
