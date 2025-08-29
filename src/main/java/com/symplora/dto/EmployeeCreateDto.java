package com.symplora.dto;

import java.time.LocalDate;

public class EmployeeCreateDto {
	private String name;
	private String email;
	private String department;
	private LocalDate joiningDate;
	private Integer initialLeaveBalance; 

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public LocalDate getJoiningDate() {
		return joiningDate;
	}

	public void setJoiningDate(LocalDate joiningDate) {
		this.joiningDate = joiningDate;
	}

	public Integer getInitialLeaveBalance() {
		return initialLeaveBalance;
	}

	public void setInitialLeaveBalance(Integer initialLeaveBalance) {
		this.initialLeaveBalance = initialLeaveBalance;
	}
}
