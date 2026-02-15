package com.notification.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.notification.entity.NotificationRequest;

@Repository
public interface NotificationRequestRepository extends JpaRepository<NotificationRequest, UUID>{

}
