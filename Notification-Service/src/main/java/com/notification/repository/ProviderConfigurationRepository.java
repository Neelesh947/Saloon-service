package com.notification.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.notification.entity.ProviderConfiguration;
import com.notification.enums.NotificationChannel;
import com.notification.strategy.ChannelStrategy;

@Repository
public interface ProviderConfigurationRepository extends JpaRepository<ProviderConfiguration, UUID>{

	Optional<ProviderConfiguration> findByChannelTypeAndTenantIdAndActiveTrue(NotificationChannel channelType,
			String tenantId);

}
