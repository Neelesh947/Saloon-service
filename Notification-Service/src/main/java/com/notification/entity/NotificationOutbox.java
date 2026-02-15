package com.notification.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "notification_outbox")
@Getter
@Setter
public class NotificationOutbox {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	private String aggregateType;

	private String aggregateId;

	@Column(columnDefinition = "JSONB")
	private String payload;

	private Boolean processed;

	private LocalDateTime createdAt;
}
