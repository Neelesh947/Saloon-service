package com.service.catalog.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.common.entity.UserServiceMapping;

@Repository
public interface UserServiceMappingRepository extends JpaRepository<UserServiceMapping, UUID>{

	List<UserServiceMapping> findByLinkedUserId(String userId);
}
