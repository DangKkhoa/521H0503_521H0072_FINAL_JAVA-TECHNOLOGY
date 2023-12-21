package com.dkkhoa.possystem.model.sales;

import com.dkkhoa.possystem.model.customers.Customer;
import com.dkkhoa.possystem.model.customers.CustomerRepository;
import com.dkkhoa.possystem.model.users.User;
import com.dkkhoa.possystem.model.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;

@Service
public class SaleService {

    @Autowired
    SaleRepository saleRepo;

    @Autowired
    UserRepository userRepo;

    @Autowired
    CustomerRepository customerRepo;


    public boolean addSale(String saleId, int totalQuantity, int totalPrice, int amountGivenByCustomer, int changeToCustomer, int userId, String phoneNumber, LocalDate saleDate, LocalTime saleTime) {
        try {
            User user = userRepo.getUserByUserId(userId);
            Customer customer = customerRepo.getCustomerByPhoneNumber(phoneNumber);

            Sale newSale = new Sale();
            newSale.setSaleId(saleId);
            newSale.setTotalQuantity(totalQuantity);
            newSale.setTotalPrice(totalPrice);
            newSale.setAmountGivenByCustomer(amountGivenByCustomer);
            newSale.setChangeToCustomer(changeToCustomer);
            newSale.setUser(user);
            newSale.setCustomer(customer);
            newSale.setSaleDate(saleDate);
            newSale.setSaleTime(saleTime);
            saleRepo.save(newSale);
            return true;
        }
        catch (Exception e) {
            System.out.println("Error occured: " + e);
            return false;
        }
    }
}
