package com.notification.sender;

import org.springframework.stereotype.Component;

import com.notification.entity.NotificationRequest;
import com.notification.enums.NotificationChannel;
import com.notification.service.ProviderResolverService;
import com.notification.strategy.ChannelStrategy;

@Component
public class EmailNotificationSender implements NotificationSender {

	private final ProviderResolverService providerResolver;

	public EmailNotificationSender(ProviderResolverService providerResolver) {
		this.providerResolver = providerResolver;
	}

	@Override
	public NotificationChannel getChannelType() {
		return NotificationChannel.EMAIL;
	}

	@Override
	public void send(NotificationRequest request, String subject, String body) {
		ChannelStrategy strategy = providerResolver.resolve(NotificationChannel.EMAIL, request.getUserId());
		strategy.send(request.getRecipient(), subject, body);
	}
}
