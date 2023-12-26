package com.dkkhoa.possystem.model.customers;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PurchaseHistoryRepository extends JpaRepository<PurchaseHistory, Long> {
    List<PurchaseHistory> findByCustomerOrderByPurchaseDateDesc(Customer customer);
}