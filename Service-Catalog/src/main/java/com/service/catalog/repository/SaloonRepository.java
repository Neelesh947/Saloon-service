package com.service.catalog.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.common.entity.Salon;

@Repository
public interface SaloonRepository extends JpaRepository<Salon, UUID> {

	boolean existsBySaloonName(String saloonName);

	Page<Salon> findByActive(Boolean active, Pageable pageable);

	Page<Salon> findByActiveAndCreatedBy(Boolean active, String createdBy, Pageable pageable);

	Page<Salon> findByCreatedBy(String createdBy, Pageable pageable);

	Optional<Salon> findByCreatedBy(String salonId);
}
