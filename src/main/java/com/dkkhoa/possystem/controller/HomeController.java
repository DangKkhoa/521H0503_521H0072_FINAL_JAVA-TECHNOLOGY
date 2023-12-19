package com.dkkhoa.possystem.controller;
import com.dkkhoa.possystem.model.saledetail.SaleDetailRepository;
import com.dkkhoa.possystem.model.saledetail.TopFiveProducts;
import com.dkkhoa.possystem.model.users.User;
import com.dkkhoa.possystem.model.users.UserRepository;
import com.dkkhoa.possystem.model.customers.CustomerRepository;
import com.dkkhoa.possystem.model.sales.SaleRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {
    @Autowired
    UserRepository userRepo;

    @Autowired
    SaleRepository saleRepo;

    @Autowired
    CustomerRepository customerRepo;

    @Autowired
    SaleDetailRepository saledetailRepo;


    @GetMapping("/")
//    @ResponseBody
    public String homePage(Model model, HttpSession session) {
        model.addAttribute("page_title", "I just changed the text");
//        Iterable<Users> users = repo.findAll();
//        List<Users> user = repo.findById(1);
        long orderQuantity = saleRepo.count();
        int totalRevenue = saleRepo.getTotalRevenue();
        long salespersonQuantity = userRepo.countByIsAdminFalse();
        long  customerQuantity = customerRepo.count();
        List<TopFiveProducts> top5Products = saledetailRepo.findTop5Products();
//        System.out.println(orders);
        System.out.println(totalRevenue);
        System.out.println(salespersonQuantity);
        System.out.println(customerQuantity);

        for(TopFiveProducts product: top5Products) {
            String name = (String) product.getProductName();
            long quantity =  product.getQuantitySold();
            System.out.println("Product name: " + name + " - " + "Quantity: " + quantity);
        }
        User user = (User) session.getAttribute("user");
        System.out.println("Username: " + user.getUsername());
        model.addAttribute("user", user);
        model.addAttribute("orderQuantity", orderQuantity);
        model.addAttribute("totalRevenue", totalRevenue);
        model.addAttribute("salespersonQuantity", salespersonQuantity);
        model.addAttribute("customerQuantity", customerQuantity);
        model.addAttribute("top5Products", top5Products);
        return "home";
    }

}
