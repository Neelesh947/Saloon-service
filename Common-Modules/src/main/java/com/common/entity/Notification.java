package com.common.entity;

import java.time.LocalDateTime;

import com.common.enums.NotificationChannels;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Notification extends BaseEntity {

	private String referenceId;

	@Enumerated(EnumType.STRING)
	private NotificationChannels channel;
	
	private Integer retryCount;
    private Integer maxRetry;
    
    private LocalDateTime scheduledTime;
}
