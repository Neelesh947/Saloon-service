package com.common.utils;

import java.util.Map;

import com.common.enums.NotificationChannels;
import com.common.enums.NotificationType;

import lombok.experimental.UtilityClass;

@UtilityClass
public class NotificationPayload {

//	private final TemplateEngine 

	/**
	 * Generate a NotificationPayload based on type, channel, and parameters.
	 *
	 * @param type      Notification type (Booking, Reminder, etc.)
	 * @param channel   SMS, Email, Push, WhatsApp
	 * @param recipient Recipient identifier (phone/email/device token)
	 * @param params    Map of dynamic parameters for template
	 * @return NotificationPayload ready to send
	 */
	public NotificationPayload generateNotification(NotificationType type, NotificationChannels channel,
			String recipient, Map<String, Object> params) {

		String templateId = getTemplateId(type, channel);
		return null;
	}

	private String getTemplateId(NotificationType type, NotificationChannels channel) {
		return type.name() + "_" + channel.name();
	}
	
	
}
