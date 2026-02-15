package com.notification.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.notification.entity.NotificationIdempotency;

@Repository
public interface NotificationIdempotencyRepository extends JpaRepository<NotificationIdempotency, UUID>{

	boolean existsByEventId(String eventId);

}
