package com.common.entity;

import java.time.LocalDateTime;

import com.common.enums.RequestStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "salon_signup_requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SalonSignupRequest extends BaseEntity{

	private String ownerName;
    private String email;
    private String mobile;
    private String salonName;
    @Enumerated(EnumType.STRING)
    private RequestStatus status;
    
	private LocalDateTime reviewedAt;
    private String reviewedBy;
    private String realm;
    private String remarks;
}
