package com.udacity.jdnd.course3.critter.repository;

import com.udacity.jdnd.course3.critter.user.Customer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Long> {
}