package com.dkkhoa.possystem.model.customers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

// CustomerService.java
@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private PurchaseHistoryRepository purchaseHistoryRepository;

    public Customer getOrCreateCustomer(String phoneNumber, String name, String address) {
        Customer existingCustomer = customerRepository.findByPhoneNumber(phoneNumber);

        if (existingCustomer != null) {
            return existingCustomer;
        } else {
            Customer newCustomer = new Customer();
            newCustomer.setFullname(name);
            newCustomer.setPhoneNumber(phoneNumber);
            newCustomer.setAddress(address);
            return customerRepository.save(newCustomer);
        }
    }
    public List<PurchaseHistory> getPurchaseHistoryByCustomer(Customer customer) {
        return purchaseHistoryRepository.findByCustomerOrderByPurchaseDateDesc(customer);
    }


    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }



    public Customer getCustomerById(Long customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new NoSuchElementException("Customer not found with id: " + customerId));
    }
}

