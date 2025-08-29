package com.symplora.service;

import com.symplora.model.Employee;
import java.util.Optional;

public interface EmployeeService {
	Employee createEmployee(Employee employee);

	Optional<Employee> getById(Long id);

	Employee update(Employee employee);
}
