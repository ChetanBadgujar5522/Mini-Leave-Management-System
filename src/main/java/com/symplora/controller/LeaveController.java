package com.symplora.controller;

import com.symplora.dto.ApplyLeaveRequestDto;
import com.symplora.model.LeaveRequest;
import com.symplora.service.LeaveService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/leaves")
public class LeaveController {

	private final LeaveService leaveService;

	public LeaveController(LeaveService leaveService) {
		this.leaveService = leaveService;
	}

	@PostMapping
	public ResponseEntity<?> applyLeave(@RequestBody ApplyLeaveRequestDto dto) {
		LeaveRequest req = new LeaveRequest();
		req.setEmployeeId(dto.getEmployeeId());
		req.setStartDate(dto.getStartDate());
		req.setEndDate(dto.getEndDate());
		req.setReason(dto.getReason());
		LeaveRequest saved = leaveService.applyForLeave(req);
		return ResponseEntity.ok(saved);
	}

	@PutMapping("/{id}/approve")
	public ResponseEntity<?> approveLeave(@PathVariable Long id,
			@RequestParam(name = "by", required = false, defaultValue = "HR") String by) {
		LeaveRequest updated = leaveService.approveLeave(id, by);
		return ResponseEntity.ok(updated);
	}

	@PutMapping("/{id}/reject")
	public ResponseEntity<?> rejectLeave(@PathVariable Long id,
			@RequestParam(name = "by", required = false, defaultValue = "HR") String by,
			@RequestParam(name = "reason", required = false) String reason) {
		LeaveRequest updated = leaveService.rejectLeave(id, by, reason);
		return ResponseEntity.ok(updated);
	}
}
