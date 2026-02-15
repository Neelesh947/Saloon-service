package com.notification.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.notification.entity.NotificationPreference;
import com.notification.enums.EventType;
import com.notification.enums.NotificationChannel;

@Repository
public interface NotificationPreferenceRepository extends JpaRepository<NotificationPreference, UUID>{

	Optional<NotificationPreference>  findByUserIdAndChannelTypeAndEventType(String userId, NotificationChannel channelType, EventType eventType);

}
