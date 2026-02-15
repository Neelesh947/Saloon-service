package com.notification.strategy;

import java.util.List;

import org.springframework.stereotype.Component;

import com.notification.enums.NotificationChannel;
import com.notification.sender.NotificationSender;

@Component
public class NotificationFactory {

	private final List<NotificationSender> senders;
	
	public NotificationFactory(List<NotificationSender> senders) {
		this.senders = senders;
	}

	public NotificationSender getSender(NotificationChannel channelType) {
		return senders.stream().filter(sender -> sender.getChannelType() == channelType).findFirst()
				.orElseThrow(() -> new RuntimeException("Sender not found"));
	}
}
