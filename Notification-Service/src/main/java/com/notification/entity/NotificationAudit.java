package com.notification.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "notification_audit")
public class NotificationAudit {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	private String entityName;

	private String entityId;

	private String action; // CREATED, UPDATED, DELETED

	private String performedBy;

	private LocalDateTime performedAt;
}
