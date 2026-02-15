package com.notification.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import com.notification.enums.NotificationStatus;
import com.notification.enums.ProviderType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "notification_logs")
@Getter
@Setter
public class NotificationLog {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@ManyToOne
	@JoinColumn(name = "request_id")
	private NotificationRequest request;

	private String providerMessageId;

	@Enumerated(EnumType.STRING)
	private ProviderType providerType;

	@Enumerated(EnumType.STRING)
	private NotificationStatus status;

	@Column(columnDefinition = "TEXT")
	private String errorMessage;

	private Integer retryAttempt;

	private LocalDateTime sentAt;
	private LocalDateTime createdAt;
}
