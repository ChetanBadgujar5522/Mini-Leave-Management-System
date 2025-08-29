package com.symplora.service;

import com.symplora.model.Employee;
import com.symplora.model.LeaveRequest;
import com.symplora.model.LeaveTransaction;
import com.symplora.exception.BadRequestException;
import com.symplora.exception.NotFoundException;
import com.symplora.repository.EmployeeRepository;
import com.symplora.repository.LeaveRequestRepository;
import com.symplora.repository.LeaveTransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.temporal.ChronoUnit;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Service
public class LeaveServiceImpl implements LeaveService {

	private final LeaveRequestRepository leaveRepo;
	private final EmployeeRepository employeeRepo;
	private final LeaveTransactionRepository transactionRepo;

	public LeaveServiceImpl(LeaveRequestRepository leaveRepo, EmployeeRepository employeeRepo,
			LeaveTransactionRepository transactionRepo) {
		this.leaveRepo = leaveRepo;
		this.employeeRepo = employeeRepo;
		this.transactionRepo = transactionRepo;
	}

	@Override
	@Transactional
	public LeaveRequest applyForLeave(LeaveRequest request) {
		if (request.getStartDate() == null || request.getEndDate() == null || request.getEmployeeId() == null) {
			throw new BadRequestException("startDate, endDate and employeeId are required");
		}
		if (request.getEndDate().isBefore(request.getStartDate())) {
			throw new BadRequestException("endDate cannot be before startDate");
		}

		Employee emp = employeeRepo.findById(request.getEmployeeId())
				.orElseThrow(() -> new NotFoundException("Employee not found"));

		int daysRequested = calculateDays(request.getStartDate(), request.getEndDate());
		if (daysRequested <= 0) {
			throw new BadRequestException("Invalid number of days requested");
		}

		if (emp.getLeaveBalance() == null) {
			emp.setLeaveBalance(0);
		}

		if (daysRequested > emp.getLeaveBalance()) {
			throw new BadRequestException("Insufficient leave balance");
		}

		List<LeaveRequest.LeaveStatus> statuses = Arrays.asList(LeaveRequest.LeaveStatus.PENDING,
				LeaveRequest.LeaveStatus.APPROVED);
		List<LeaveRequest> overlaps = leaveRepo
				.findByEmployeeIdAndStatusInAndStartDateLessThanEqualAndEndDateGreaterThanEqual(request.getEmployeeId(),
						statuses, request.getEndDate(), request.getStartDate());
		if (!overlaps.isEmpty()) {
			throw new BadRequestException("Overlapping leave exists");
		}

		request.setStatus(LeaveRequest.LeaveStatus.PENDING);
		LeaveRequest saved = leaveRepo.save(request);

		LeaveTransaction tx = new LeaveTransaction(saved.getId(), "CREATED", "SYSTEM", "Applied by employee");
		transactionRepo.save(tx);

		return saved;
	}

	@Override
	@Transactional
	public LeaveRequest approveLeave(Long leaveRequestId, String performedBy) {
		LeaveRequest lr = leaveRepo.findById(leaveRequestId)
				.orElseThrow(() -> new NotFoundException("Leave request not found"));

		if (lr.getStatus() != LeaveRequest.LeaveStatus.PENDING) {
			throw new BadRequestException("Only pending leaves can be approved");
		}

		Employee emp = employeeRepo.findById(lr.getEmployeeId())
				.orElseThrow(() -> new NotFoundException("Employee not found"));

		int days = calculateDays(lr.getStartDate(), lr.getEndDate());
		if (emp.getLeaveBalance() < days) {
			throw new BadRequestException("Employee does not have enough leave balance");
		}
		
		emp.setLeaveBalance(emp.getLeaveBalance() - days);
		employeeRepo.save(emp);

		lr.setStatus(LeaveRequest.LeaveStatus.APPROVED);
		LeaveRequest updated = leaveRepo.save(lr);

		transactionRepo
				.save(new LeaveTransaction(updated.getId(), "APPROVED", performedBy, "Approved by " + performedBy));

		return updated;
	}

	@Override
	@Transactional
	public LeaveRequest rejectLeave(Long leaveRequestId, String performedBy, String reason) {
		LeaveRequest lr = leaveRepo.findById(leaveRequestId)
				.orElseThrow(() -> new NotFoundException("Leave request not found"));

		if (lr.getStatus() != LeaveRequest.LeaveStatus.PENDING) {
			throw new BadRequestException("Only pending leaves can be rejected");
		}

		lr.setStatus(LeaveRequest.LeaveStatus.REJECTED);
		LeaveRequest updated = leaveRepo.save(lr);

		transactionRepo.save(
				new LeaveTransaction(updated.getId(), "REJECTED", performedBy, reason == null ? "Rejected" : reason));

		return updated;
	}

	@Override
	public Integer getLeaveBalanceForEmployee(Long employeeId) {
		Employee emp = employeeRepo.findById(employeeId).orElseThrow(() -> new NotFoundException("Employee not found"));
		return emp.getLeaveBalance();
	}

	@Override
	public List<LeaveRequest> listLeavesForEmployee(Long employeeId) {
		return leaveRepo.findAll().stream().filter(lr -> lr.getEmployeeId().equals(employeeId)).toList();
	}

	private int calculateDays(LocalDate start, LocalDate end) {
		long days = ChronoUnit.DAYS.between(start, end) + 1;
		return (int) days;
	}
}
