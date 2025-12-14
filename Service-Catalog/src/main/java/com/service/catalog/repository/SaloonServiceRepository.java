package com.service.catalog.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.common.entity.SaloonService;

@Repository
public interface SaloonServiceRepository extends JpaRepository<SaloonService, UUID> {

	List<SaloonService> findByCategory(String category);
}
