package com.common.entity;

import java.util.List;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "staff")
public class Staff extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(unique = true)
    private String email;

    @Column
    private String phone;

    @Column(nullable = false, unique = true)
    private String createdBy;

    // List of service IDs provided by this staff (microservice-friendly)
    /**
     * List of services assigned to this staff.
     * These must exist in the service table in the DB.
     */
    @ElementCollection
    @Column(name = "service_id")
    private List<UUID> serviceIds;
}