package com.notification.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import com.notification.enums.EventType;
import com.notification.enums.NotificationChannel;
import com.notification.enums.NotificationStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "notification_requests", indexes = @Index(name = "idx_event_id", columnList = "eventId"))
@Getter
@Setter
public class NotificationRequest {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@Column(nullable = false)
	private String eventId;

	@Enumerated(EnumType.STRING)
	private EventType eventType;

	@Enumerated(EnumType.STRING)
	private NotificationChannel channelType;

	@Column(nullable = false)
	private String recipient;

	@Column(columnDefinition = "JSONB")
	private String payload; // template variables as JSON

	@Enumerated(EnumType.STRING)
	private NotificationStatus status;

	private LocalDateTime scheduledAt;

	private Integer retryCount;
	
	private String userId;

	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
