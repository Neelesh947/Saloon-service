package com.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "admin_staff_mapping")
@Getter
@Setter
public class AdminAndStaffMapping extends BaseEntity{

	@Column(nullable = false)
    private String linkedAdminId;
	
	@Column(nullable = false)
    private String linkedStaffId;
}
