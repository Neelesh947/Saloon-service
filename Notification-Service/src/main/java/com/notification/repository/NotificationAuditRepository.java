package com.notification.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.notification.entity.NotificationAudit;

@Repository
public interface NotificationAuditRepository extends JpaRepository<NotificationAudit, UUID>{

}
