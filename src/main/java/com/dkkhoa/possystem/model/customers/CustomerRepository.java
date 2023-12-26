package com.dkkhoa.possystem.model.customers;

import com.dkkhoa.possystem.model.products.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Customer findByPhoneNumber(String phoneNumber);



}