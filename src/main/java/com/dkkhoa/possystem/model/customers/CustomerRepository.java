package com.dkkhoa.possystem.model.customers;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Integer> {
    Customer getCustomerByPhoneNumber(String phoneNumber);
}
