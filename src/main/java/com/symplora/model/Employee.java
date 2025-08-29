package com.symplora.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "employees", indexes = { @Index(columnList = "email", name = "idx_employee_email") })
public class Employee {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	@Column(nullable = false, unique = true)
	private String email;

	private String department;

	private LocalDate joiningDate;

	private Integer leaveBalance;

	public Employee() {
	}

	public Employee(String name, String email, String department, LocalDate joiningDate, Integer leaveBalance) {
		this.name = name;
		this.email = email;
		this.department = department;
		this.joiningDate = joiningDate;
		this.leaveBalance = leaveBalance;
	}
}
