package com.service.catalog.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.common.entity.AdminAndStaffMapping;

@Repository
public interface AdminAndStaffMappingRepository extends JpaRepository<AdminAndStaffMapping, UUID> {

	List<AdminAndStaffMapping> findByLinkedAdminId(String adminKeycloakId);
}
