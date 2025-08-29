package com.symplora.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "leave_transactions")
public class LeaveTransaction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long leaveRequestId;

	private String action;
	private String performedBy;
	private LocalDateTime timestamp;
	@Column(length = 1000)
	private String comment;

	public LeaveTransaction() {
	}

	public LeaveTransaction(Long leaveRequestId, String action, String performedBy, String comment) {
		this.leaveRequestId = leaveRequestId;
		this.action = action;
		this.performedBy = performedBy;
		this.comment = comment;
		this.timestamp = LocalDateTime.now();
	}
}
