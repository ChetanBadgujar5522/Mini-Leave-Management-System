package com.symplora.service;

import com.symplora.model.Employee;
import com.symplora.exception.NotFoundException;
import com.symplora.repository.EmployeeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	private final EmployeeRepository repo;

	public EmployeeServiceImpl(EmployeeRepository repo) {
		this.repo = repo;
	}

	@Override
	public Employee createEmployee(Employee employee) {
		return repo.save(employee);
	}

	@Override
	public java.util.Optional<Employee> getById(Long id) {
		return repo.findById(id);
	}

	@Override
	@Transactional
	public Employee update(Employee employee) {
		if (employee.getId() == null || !repo.existsById(employee.getId())) {
			throw new NotFoundException("Employee not found");
		}
		return repo.save(employee);
	}
}
