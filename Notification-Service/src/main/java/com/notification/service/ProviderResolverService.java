package com.notification.service;

import java.nio.file.ProviderNotFoundException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.notification.entity.ProviderConfiguration;
import com.notification.enums.NotificationChannel;
import com.notification.repository.ProviderConfigurationRepository;
import com.notification.strategy.ChannelStrategy;

@Service
public class ProviderResolverService {

	private final ProviderConfigurationRepository configRepository;
	private final List<ChannelStrategy> strategies;

	public ProviderResolverService(ProviderConfigurationRepository configRepository, List<ChannelStrategy> strategies) {
		this.configRepository = configRepository;
		this.strategies = strategies;
	}

	public ChannelStrategy resolve(NotificationChannel channelType, String tenantId) {

		ProviderConfiguration config = configRepository.findByChannelTypeAndTenantIdAndActiveTrue(channelType, tenantId)
				.orElseThrow(() -> new ProviderNotFoundException("Active provider not found"));

		return strategies.stream()
				.filter(strategy -> strategy.getProviderType() == config.getProviderType()
						&& strategy.getChannelType() == channelType)
				.findFirst().orElseThrow(() -> new ProviderNotFoundException("Strategy implementation missing"));
	}
}
