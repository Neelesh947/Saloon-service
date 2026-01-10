package com.common.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.common.enums.NotificationChannels;
import com.common.enums.NotificationType;

import lombok.experimental.UtilityClass;

@UtilityClass
public class NotificationPayload {

	/**
	 * Generate a NotificationPayload based on type, channel, and parameters.
	 * 
	 * @param type         Notification type (Booking, Reminder, etc.)
	 * @param channel      SMS, Email, Push, WhatsApp
	 * @param recipient    Recipient identifier (phone/email/device token)
	 * @param extraDetails Map of dynamic parameters for template/body
	 * @return NotificationPayload ready to send
	 */
	public Map<String, Object> generateNotificationPayload(NotificationType type, NotificationChannels channel,
			String recipient, Map<String, Object> extraDetails) {
		validate(type, channel, recipient);
		String templateId = getTemplateId(type, channel);
		Map<String, Object> payload = new HashMap<>();
		payload.put("type", type.name());
		payload.put("channel", channel.name());
		payload.put("recipient", recipient);
		payload.put("templateId", templateId);
		if (extraDetails != null && !extraDetails.isEmpty()) {
			payload.putAll(extraDetails);
		}
		return payload;
	}

	/**
	 * Resolve template id based on notification type and channel. Example:
	 * BOOKING_SMS, REMINDER_EMAIL
	 */
	private String getTemplateId(NotificationType type, NotificationChannels channel) {
		return type.name() + "_" + channel.name();
	}

	private void validate(NotificationType type, NotificationChannels channel, String recipient) {

		Objects.requireNonNull(type, "NotificationType must not be null");
		Objects.requireNonNull(channel, "NotificationChannel must not be null");

		if (recipient == null || recipient.isBlank()) {
			throw new IllegalArgumentException("Recipient must not be null or empty");
		}
	}
}
