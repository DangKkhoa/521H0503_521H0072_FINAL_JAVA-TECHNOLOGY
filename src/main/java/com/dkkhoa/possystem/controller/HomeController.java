package com.dkkhoa.possystem.controller;
import com.dkkhoa.possystem.model.customers.CustomerRepository;
import com.dkkhoa.possystem.model.saledetail.SaleDetailRepository;
import com.dkkhoa.possystem.model.saledetail.TopFiveProducts;
import com.dkkhoa.possystem.model.users.SessionUser;
import com.dkkhoa.possystem.model.users.UserRepository;
import com.dkkhoa.possystem.model.sales.SaleRepository;
import jakarta.servlet.http.HttpServletResponse;
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


    @GetMapping({"/home", "/"})
//    @ResponseBody
    public String homePage(Model model, HttpSession session, HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");

        SessionUser user = (SessionUser) session.getAttribute("user");

        if(user == null ) {
            return "redirect:/login";
        }

        model.addAttribute("page_title", "I just changed the text");

        long orderQuantity = saleRepo.count();
        int totalRevenue = saleRepo.getTotalRevenue();
        long salespersonQuantity = userRepo.countByIsAdminFalse();
        long  customerQuantity = customerRepo.count();
        List<TopFiveProducts> top5Products = saledetailRepo.findTop5Products();

        model.addAttribute("user", user);
        model.addAttribute("orderQuantity", orderQuantity);
        model.addAttribute("totalRevenue", totalRevenue);
        model.addAttribute("salespersonQuantity", salespersonQuantity);
        model.addAttribute("customerQuantity", customerQuantity);
        model.addAttribute("top5Products", top5Products);
        return "home";
    }

}
