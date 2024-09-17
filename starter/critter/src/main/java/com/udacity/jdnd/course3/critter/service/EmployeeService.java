package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.repository.EmployeeRepository;
import com.udacity.jdnd.course3.critter.user.Employee;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    // service constructor
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    // save the employee
    public Long save(Employee employee) {
        employeeRepository.save(employee);

        return employee.getId();
    }

    // find employee, param is id
    public Optional<Employee> findById(Long employeeId) {
        return employeeRepository.findById(employeeId);
    }
}