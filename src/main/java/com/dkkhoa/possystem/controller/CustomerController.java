package com.dkkhoa.possystem.controller;

import com.dkkhoa.possystem.model.customers.Customer;
import com.dkkhoa.possystem.model.customers.CustomerRepository;
import com.dkkhoa.possystem.model.customers.CustomerService;
import com.dkkhoa.possystem.model.customers.PurchaseHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private CustomerService customerService;

    @GetMapping("/list")
    public String showCustomerList(Model model) {
        List<Customer> customers = customerRepository.findAll();
        model.addAttribute("customers", customers);
        return "customers/customer-list";
    }

    @GetMapping

    public String showCheckoutForm(Model model) {
        model.addAttribute("customer", new Customer());
        model.addAttribute("newCustomer", new Customer());

        return "customers/checkout";

    }

    @PostMapping("/checkout")
    public String processCheckoutForm(@ModelAttribute Customer customer, Model model) {
        Customer existingCustomer = customerRepository.findByPhoneNumber(customer.getPhoneNumber());
        if (existingCustomer != null) {
            model.addAttribute("customer", existingCustomer);
            return "customers/result";

        } else {
            model.addAttribute("newCustomer", customer);
            return "customers/fill";
        }


    }
    @PostMapping("/fill")
    public String fillCustomer(@ModelAttribute Customer newCustomer, Model model) {
        model.addAttribute("customer", newCustomer);

        customerRepository.save(newCustomer);

        return "customers/result";
    }
    @GetMapping("/{customerId}/purchase-history")
    public String viewPurchaseHistory(@PathVariable Long customerId, Model model) {
        Customer customer = customerService.getCustomerById(customerId);
        List<PurchaseHistory> purchaseHistoryList = customerService.getPurchaseHistoryByCustomer(customer);
        System.out.println("Purchase History List: " + purchaseHistoryList);
        model.addAttribute("customer", customer);
        model.addAttribute("purchaseHistoryList", purchaseHistoryList);
        return "customers/purchase-history";
    }

}
