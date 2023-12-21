package com.dkkhoa.possystem.controller;

import com.dkkhoa.possystem.model.products.Product;
import com.dkkhoa.possystem.model.products.ProductRepository;
import com.dkkhoa.possystem.model.products.ProductSelected;
import com.dkkhoa.possystem.model.saledetail.SaleDetailService;
import com.dkkhoa.possystem.model.sales.Sale;
import com.dkkhoa.possystem.model.sales.SaleService;
import com.dkkhoa.possystem.model.users.SessionUser;
import com.dkkhoa.possystem.model.users.User;
import jakarta.servlet.http.HttpSession;
import org.apache.coyote.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
@RequestMapping("/transaction")
public class TransactionController {

    private String iDGenerator() {
        UUID uuid = UUID.randomUUID();
        String saleId = uuid.toString().replace("-", "").substring(0, 12);
        return saleId;
    }

    private String saleId = iDGenerator();
    @Autowired
    private ProductRepository productRepo;

    @Autowired
    private SaleDetailService saledetailService;

    @Autowired
    private SaleService saleService;
    @GetMapping("")
    public String transaction(HttpSession session, Model model) {

        if(saleId == null) {
             saleId = iDGenerator();
        }

        SessionUser user = (SessionUser) session.getAttribute("user");
        if(user == null) {
            return "redirect:/login";
        }

        Iterable<Product> products = productRepo.findAll();
        products.forEach(p -> System.out.println(p.getProductName()));
//        System.out.println(products);
        model.addAttribute("products", products);
        model.addAttribute("user", user);
        model.addAttribute("saleId", saleId);
        return "transaction";
    }

    @PostMapping("/processing")
    @ResponseBody
    private LinkedHashMap<String, Object> processing(@RequestBody LinkedHashMap<String, Object> saleData, HttpSession session) {
        SessionUser user = (SessionUser) session.getAttribute("user");
        System.out.println(user.getId());

        List<Map<String, Object>> products_selected = (List<Map<String, Object>>) saleData.get("products");
        System.out.println(products_selected);

        LocalDate saleDate = LocalDate.parse(saleData.get("date").toString());
        LocalTime saleTime = LocalTime.parse(saleData.get("time").toString());
        int amountGivenByCustomer = Integer.parseInt(saleData.get("amount_given_by_customer").toString());
        int changeToCustomer = Integer.parseInt(saleData.get("change_to_customer").toString());
        int totalQuantity = Integer.parseInt(saleData.get("total_quantity").toString());
        int totalPrice =Integer.parseInt(saleData.get("total_cost").toString());
//        System.out.println(products);
//        System.out.println(saleId);
//        System.out.println(totalPrice);
//        System.out.println(totalQuantity);
//        System.out.println(amountGivenByCustomer);
//        System.out.println(changeToCustomer);
//        System.out.println(saleDate);
//        System.out.println(saleTime);
        boolean isAdded = saleService.addSale(saleId, totalQuantity, totalPrice, amountGivenByCustomer, changeToCustomer, user.getId(), null, saleDate, saleTime);
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        if(isAdded) {
            products_selected.forEach(p -> {
                System.out.println(p);
                ProductSelected productSelectedDetail = saledetailService.format(p);
                saledetailService.saledetailAdd(productSelectedDetail, saleId);
                saleId = null;
            });

            response.put("code", 1);
            response.put("message", "Transaction stored successfully");
            return response;
        }
        response.put("code", 0);
        response.put("message", "Error occured. Please try again later");
        return response;
    }

    @GetMapping("/processing")
    private String trasactionStautus(HttpSession session, @RequestParam(name = "complete", defaultValue = "false") boolean complete, Model model) {
        if(session.getAttribute("user") == null) {
            return "redirect:/";
        }
        model.addAttribute("complete", complete);

        return "transactionStatus";



    }




}
