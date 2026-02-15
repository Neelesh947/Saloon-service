package com.notification.strategy;

import org.springframework.stereotype.Component;

import com.notification.enums.NotificationChannel;
import com.notification.enums.ProviderType;

@Component
public class SinchSmsStrategy implements ChannelStrategy{
	
	@Override
	public ProviderType getProviderType() {
		return ProviderType.SINCH;
	}

	@Override
	public NotificationChannel getChannelType() {
		return NotificationChannel.SMS;
	}

	@Override
	public void send(String recipient, String subject, String body) {
		
	}

}
