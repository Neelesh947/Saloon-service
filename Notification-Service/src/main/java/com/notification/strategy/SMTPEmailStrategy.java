package com.notification.strategy;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.notification.enums.NotificationChannel;
import com.notification.enums.ProviderType;

@Component
public class SMTPEmailStrategy implements ChannelStrategy {

	private final JavaMailSender javaMailSender;

	public SMTPEmailStrategy(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}

	@Override
	public ProviderType getProviderType() {
		return ProviderType.SMTP;
	}

	@Override
	public NotificationChannel getChannelType() {
		return NotificationChannel.EMAIL;
	}

	@Override
	public void send(String recipient, String subject, String body) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(recipient);
		message.setSubject(subject);
		message.setText(body);
		javaMailSender.send(message);
	}
}
