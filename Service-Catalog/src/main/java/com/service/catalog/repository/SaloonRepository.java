package com.service.catalog.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.common.entity.Salon;

@Repository
public interface SaloonRepository extends JpaRepository<Salon, UUID> {

	boolean existsBySaloonName(String saloonName);

	Page<Salon> findByActive(Boolean active, Pageable pageable);
}
