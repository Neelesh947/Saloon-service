package com.service.catalog.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.common.entity.SalonSignupRequest;
import com.common.enums.RequestStatus;

@Repository
public interface SalonSignupRequestRepository extends JpaRepository<SalonSignupRequest, UUID>{

	boolean existsByEmailAndStatus(String email, RequestStatus pending);

	boolean existsByMobileAndStatus(String mobile, RequestStatus pending);

	List<SalonSignupRequest> findByStatus(RequestStatus status);

}
