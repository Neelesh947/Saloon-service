package com.notification.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.notification.entity.NotificationRequest;
import com.notification.entity.NotificationTemplateVersion;
import com.notification.enums.NotificationStatus;
import com.notification.repository.NotificationRequestRepository;
import com.notification.sender.NotificationSender;
import com.notification.strategy.NotificationFactory;

@Service
public class NotificationOrchestratorService {

	private final IdempotencyService idempotencyService;
	private final PreferenceService preferenceService;
	private final TemplateService templateService;
	private final TemplateRenderService renderService;
	private final NotificationFactory notificationFactory;
	private final LogService logService;
	private final RetryService retryService;
	private final SchedulingService schedulingService;
	private final NotificationRequestRepository requestRepository;

	public NotificationOrchestratorService(IdempotencyService idempotencyService, PreferenceService preferenceService,
			TemplateService templateService, TemplateRenderService renderService,
			NotificationFactory notificationFactory, LogService logService, RetryService retryService,
			SchedulingService schedulingService, NotificationRequestRepository requestRepository) {

		this.idempotencyService = idempotencyService;
		this.preferenceService = preferenceService;
		this.templateService = templateService;
		this.renderService = renderService;
		this.notificationFactory = notificationFactory;
		this.logService = logService;
		this.retryService = retryService;
		this.schedulingService = schedulingService;
		this.requestRepository = requestRepository;
	}

	@Transactional
	public void process(NotificationRequest request) {
		if (idempotencyService.isDuplicate(request.getEventId())) {
			return;
		}
		if (!preferenceService.isAllowed(request)) {
			request.setStatus(NotificationStatus.CANCELLED);
			request.setUpdatedAt(LocalDateTime.now());
			requestRepository.save(request);
			return;
		}
		if (request.getScheduledAt() != null && request.getScheduledAt().isAfter(LocalDateTime.now())) {
			schedulingService.schedule(request);
			return;
		}

		try {
			request.setStatus(NotificationStatus.PROCESSING);
			request.setUpdatedAt(LocalDateTime.now());
			requestRepository.save(request);
			NotificationTemplateVersion template = templateService.getActiveTemplate(request.getEventType(),
					request.getChannelType(), request.getUserId() // ✅ FIXED (not userId)
			);
			String subject = renderService.render(template.getSubject(), request.getPayload());

			String body = renderService.render(template.getBody(), request.getPayload());

			NotificationSender sender = notificationFactory.getSender(request.getChannelType());

			sender.send(request, subject, body);

			request.setStatus(NotificationStatus.SENT);
			request.setUpdatedAt(LocalDateTime.now());
			requestRepository.save(request);

			logService.logSuccess(request);

			idempotencyService.markProcessed(request.getEventId());

		} catch (Exception ex) {

			request.setStatus(NotificationStatus.FAILED);
			request.setRetryCount(request.getRetryCount() == null ? 1 : request.getRetryCount() + 1);
			request.setUpdatedAt(LocalDateTime.now());
			requestRepository.save(request);

			logService.logFailure(request, ex);
			retryService.scheduleRetry(request);
			throw new InternalError("Notification failed", ex);
		}
	}
}
