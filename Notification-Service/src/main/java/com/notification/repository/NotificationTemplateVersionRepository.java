package com.notification.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.notification.entity.NotificationTemplate;
import com.notification.entity.NotificationTemplateVersion;

@Repository
public interface NotificationTemplateVersionRepository extends JpaRepository<NotificationTemplateVersion, UUID> {

	Optional<NotificationTemplateVersion> findTopByTemplateAndActiveTrueOrderByVersionNumberDesc(
			NotificationTemplate template);

}
