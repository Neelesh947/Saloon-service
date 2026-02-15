package com.notification.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import com.notification.enums.EventType;
import com.notification.enums.NotificationChannel;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "notification_preferences", uniqueConstraints = @UniqueConstraint(columnNames = { "userId", "channelType",
		"eventType" }))
@Getter
@Setter
public class NotificationPreference {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	private String userId;

	@Enumerated(EnumType.STRING)
	private NotificationChannel channelType;

	@Enumerated(EnumType.STRING)
	private EventType eventType;

	private Boolean enabled;

	private LocalDateTime updatedAt;
}
