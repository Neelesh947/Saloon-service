package com.service.catalog.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.common.entity.Salon;

@Repository
public interface SaloonRepository extends JpaRepository<Salon, UUID>{

}
