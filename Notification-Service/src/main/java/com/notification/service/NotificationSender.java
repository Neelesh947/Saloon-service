package com.notification.service;

import com.notification.entity.NotificationRequest;
import com.notification.enums.NotificationChannel;

public interface NotificationSender {

	NotificationChannel getChannelType();

	void send(NotificationRequest request, String subject, String body);
}
