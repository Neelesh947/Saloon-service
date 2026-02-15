package com.notification.strategy;

import com.notification.enums.NotificationChannel;
import com.notification.enums.ProviderType;

public interface ChannelStrategy {

	ProviderType getProviderType();

	NotificationChannel getChannelType();

	void send(String recipient, String subject, String body);
}
