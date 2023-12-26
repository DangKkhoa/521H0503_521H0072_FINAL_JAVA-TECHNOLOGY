package com.dkkhoa.possystem.model.customers;

import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "phone_number")
    private String phoneNumber;
    private String fullname;
    private String address;
    @OneToMany(mappedBy = "customer",fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<PurchaseHistory> purchaseHistory;

}
