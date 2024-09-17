package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.repository.CustomerRepository;
import com.udacity.jdnd.course3.critter.repository.EmployeeRepository;
import com.udacity.jdnd.course3.critter.user.Customer;
import com.udacity.jdnd.course3.critter.user.Employee;
import com.udacity.jdnd.course3.critter.user.EmployeeSkill;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Transactional
public class UserService {
    private final CustomerRepository customerRepository;
    private final EmployeeRepository employeeRepository;

    @PersistenceContext
    private EntityManager entityManager;

    // userService constructor
    public UserService(CustomerRepository customerRepository, EmployeeRepository employeeRepository) {
        this.customerRepository = customerRepository;
        this.employeeRepository = employeeRepository;
    }

    // save customer
    public Long save(Customer customer) {
        customerRepository.save(customer);
        return customer.getId();
    }

    // get all customers
    public List<Customer> getAllCustomers() {
        return StreamSupport.stream(customerRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    // get customer by id
    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
    }

    public Optional<Customer> getUser(Long ownerId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Customer> query = cb.createQuery(Customer.class);

        //
        Root<Customer> root = query.from(Customer.class);
        root.fetch("pets", JoinType.LEFT);

        //
        query.select(root).where(cb.equal(root.get("id"), ownerId));
        List<Customer> results = entityManager.createQuery(query).getResultList();

        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    public Optional<Employee> getEmployee(long id) {
        return employeeRepository.findById(id);
    }

    public List<Employee> findEmployeesForService(DayOfWeek desiredDate, Set<EmployeeSkill> desiredSkills) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Employee> query = cb.createQuery(Employee.class);

        //
        Root<Employee> employee = query.from(Employee.class);

        //
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.isMember(desiredDate, employee.get("daysAvailable")));

        // Check if the employee has all required skills
        for (EmployeeSkill skill : desiredSkills) {
            predicates.add(cb.isMember(skill, employee.get("skills")));
        }

        //
        query.select(employee)
                .where(cb.and(predicates.toArray(new Predicate[0])));

        return entityManager.createQuery(query).getResultList();
    }
}