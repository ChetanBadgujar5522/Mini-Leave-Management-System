package com.symplora.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "leave_requests", indexes = { @Index(columnList = "employee_id", name = "idx_leave_employee") })
public class LeaveRequest {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "employee_id", nullable = false)
	private Long employeeId;

	private LocalDate startDate;
	private LocalDate endDate;

	@Column(length = 500)
	private String reason;

	@Enumerated(EnumType.STRING)
	private LeaveStatus status;

	private LocalDateTime createdAt;

	public LeaveRequest() {
	}

	public LeaveRequest(Long employeeId, LocalDate startDate, LocalDate endDate, String reason, LeaveStatus status) {
		this.employeeId = employeeId;
		this.startDate = startDate;
		this.endDate = endDate;
		this.reason = reason;
		this.status = status;
		this.createdAt = LocalDateTime.now();
	}

	public enum LeaveStatus {
		PENDING, APPROVED, REJECTED
	}
}
