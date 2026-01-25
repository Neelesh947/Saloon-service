package com.service.catalog.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.common.entity.Staff;

@Repository
public interface StaffRepository extends JpaRepository<Staff, UUID>{

}
