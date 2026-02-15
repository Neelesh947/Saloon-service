package com.notification.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.notification.entity.NotificationLog;
import com.notification.entity.NotificationRequest;
import com.notification.enums.NotificationStatus;
import com.notification.repository.NotificationLogRepository;

@Service
public class LogService {

	private final NotificationLogRepository notificationLogRepository;
	
	public LogService(NotificationLogRepository notificationLogRepository) {
		this.notificationLogRepository = notificationLogRepository;
	}
	
	public void logSuccess(NotificationRequest request) {

        NotificationLog log = new NotificationLog();
        log.setRequest(request);
        log.setStatus(NotificationStatus.SENT);
        log.setSentAt(LocalDateTime.now());
        log.setCreatedAt(LocalDateTime.now());

        notificationLogRepository.save(log);
    }

    public void logFailure(NotificationRequest request, Exception ex) {

        NotificationLog log = new NotificationLog();
        log.setRequest(request);
        log.setStatus(NotificationStatus.FAILED);
        log.setErrorMessage(ex.getMessage());
        log.setCreatedAt(LocalDateTime.now());

        notificationLogRepository.save(log);
    }
}
