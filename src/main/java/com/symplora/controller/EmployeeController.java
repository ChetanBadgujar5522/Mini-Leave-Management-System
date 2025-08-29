package com.symplora.controller;

import com.symplora.dto.EmployeeCreateDto;
import com.symplora.dto.SimpleResponseDto;
import com.symplora.model.Employee;
import com.symplora.service.EmployeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

	private final EmployeeService employeeService;
	private static final int DEFAULT_LEAVE_BALANCE = 20;

	public EmployeeController(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	@PostMapping
	public ResponseEntity<Employee> createEmployee(@RequestBody EmployeeCreateDto dto) {
		Employee e = new Employee();
		e.setName(dto.getName());
		e.setEmail(dto.getEmail());
		e.setDepartment(dto.getDepartment());
		e.setJoiningDate(dto.getJoiningDate() == null ? LocalDate.now() : dto.getJoiningDate());
		e.setLeaveBalance(dto.getInitialLeaveBalance() == null ? DEFAULT_LEAVE_BALANCE : dto.getInitialLeaveBalance());
		Employee saved = employeeService.createEmployee(e);
		return ResponseEntity.ok(saved);
	}

	@GetMapping("/{id}/balance")
	public ResponseEntity<?> getBalance(@PathVariable Long id) {
		Integer balance = employeeService.getById(id).map(Employee::getLeaveBalance)
				.orElseThrow(() -> new com.symplora.exception.NotFoundException("Employee not found"));
		return ResponseEntity.ok(new SimpleResponseDto("Leave balance: " + balance));
	}
}
