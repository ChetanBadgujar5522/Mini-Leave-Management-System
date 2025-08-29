package com.symplora.service;

import com.symplora.model.LeaveRequest;
import java.util.List;

public interface LeaveService {
	LeaveRequest applyForLeave(LeaveRequest request);

	LeaveRequest approveLeave(Long leaveRequestId, String performedBy);

	LeaveRequest rejectLeave(Long leaveRequestId, String performedBy, String reason);

	Integer getLeaveBalanceForEmployee(Long employeeId);

	List<LeaveRequest> listLeavesForEmployee(Long employeeId);
}
