package com.common.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "salons")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Salon extends BaseEntity {

	@Column(nullable = false, unique = true)
	private String saloonName;

	private String address;

	private String phone;

	private Boolean active;

	@OneToMany(mappedBy = "salon", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<SaloonService> services;
}
