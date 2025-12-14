package com.common.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import com.common.enums.AppointmentStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

@Entity
@Table(name = "appointments")
public class Appointment extends BaseEntity {

    @Column(nullable = false)
    private String keycloakUserId; // Customer ID from Keycloak

    @Column(nullable = false)
    private UUID serviceId; // ID from Service Catalog Service

    @Column(nullable = false)
    private UUID staffId; // ID from Staff Service

    @Column(nullable = false)
    private LocalDateTime appointmentTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppointmentStatus status = AppointmentStatus.BOOKED;
}