package com.common.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "staff")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Staff extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(unique = true)
    private String email;

    @Column
    private String phone;

    @Column(nullable = false)
    private String createdBy;
    
    @Column(nullable = false)
    private String keycloakUserId;

    // List of service IDs provided by this staff (microservice-friendly)
    /**
     * List of services assigned to this staff.
     * These must exist in the service table in the DB.
     */
    @ElementCollection
    @Column(name = "service_id")
    private List<String> serviceIds;
}