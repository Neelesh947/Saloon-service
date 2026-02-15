package com.notification.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import com.notification.enums.ProviderType;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "provider_configurations")
@Getter
@Setter
public class ProviderConfiguration {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    private ProviderType providerType;

    private String tenantId;

    private String apiKey;
    private String apiSecret;
    private String host;
    private Integer port;

    private Boolean active;

    private LocalDateTime createdAt;
}
