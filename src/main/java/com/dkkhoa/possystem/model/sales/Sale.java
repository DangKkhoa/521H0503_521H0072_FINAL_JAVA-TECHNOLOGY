package com.dkkhoa.possystem.model.sales;

import com.dkkhoa.possystem.model.customers.Customer;
import com.dkkhoa.possystem.model.users.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "sales")
public class Sale {
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sale_id")
    private String saleId;
    @Column(name = "total_quantity")
    private int totalQuantity;
    @Column(name = "total_price")
    private int totalPrice;
    @Column(name = "amount_given_by_customer")
    private int amountGivenByCustomer;
    @Column(name = "change_to_customer")
    private int changeToCustomer;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
    @OneToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
    @Column(name = "sale_date")
    private LocalDate saleDate;
    @Column(name = "sale_time")
    private LocalTime saleTime;
}
