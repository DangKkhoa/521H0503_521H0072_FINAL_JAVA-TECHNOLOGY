package com.dkkhoa.possystem.controller;

import com.dkkhoa.possystem.model.saledetail.SaleDetail;
import com.dkkhoa.possystem.model.saledetail.SaleDetailRepository;
import com.dkkhoa.possystem.model.sales.Sale;
import com.dkkhoa.possystem.model.sales.SaleRepository;
import com.dkkhoa.possystem.model.sales.SaleService;
import com.dkkhoa.possystem.model.users.SessionUser;
import com.dkkhoa.possystem.model.users.User;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/sale_history")
public class SaleController {

    @Autowired
    private SaleDetailRepository saledetailRepo;

    @Autowired
    private SaleRepository saleRepo;

    @Autowired
    private SaleService saleService;


    @GetMapping("")
//    @ResponseBody
    public String sale_history(HttpSession session, HttpServletResponse response, Model model) {
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");

        SessionUser user = (SessionUser) session.getAttribute("user");

        if(user == null) {
            return "redirect:/login";
        }

        if(user.isFirstLogin() && !user.isAdmin()) {
            return "redirect:/set_password";
        }

        int totalQuantity;
        int totalRevenue;
        long totalOrder;
        Iterable<Sale> sales;

        if(!user.isAdmin()) {
            sales = saleRepo.getSaleByUserId(user.getId());
            System.out.println(sales.toString());
            totalQuantity = saleRepo.getTotalQuantityByUserId(user.getId());
            totalRevenue = saleRepo.getTotalRevenueByUserId(user.getId());
            totalOrder = saleRepo.countSaleByUserId(user.getId());
        }
        else {
            sales = saleRepo.findAll();
            totalQuantity = saleRepo.getTotalQuantity();
            totalRevenue = saleRepo.getTotalRevenue();
            totalOrder = saleRepo.count();
        }

        model.addAttribute("sales", sales);
        model.addAttribute("totalQuantity", totalQuantity);
        model.addAttribute("totalRevenue", totalRevenue);
        model.addAttribute("totalOrder", totalOrder);
        model.addAttribute("user", user);

        return "saleHistory";
    }

//    @CrossOrigin
    @PostMapping("/data")
    @ResponseBody
    public Map<String, Object> send_data(HttpSession session) {
        SessionUser user = (SessionUser) session.getAttribute("user");
        Map<String, Object> error = new LinkedHashMap<>();
        if(user == null) {
            error.put("code", 2);
            error.put("message", "Login to continue");

            return error;
        }

        if(user.isFirstLogin() && !user.isAdmin()) {
            error.put("code", 3);
            error.put("message", "Login and change password to continue");

            return error;
        }
        // If admin, return monthly profit
        if(user.isAdmin()) {
            List<Object[]> profitEveryMonth = saledetailRepo.getAllMonthProfit();

            List<Map<String, Object>> monthlyProfitObject = profitEveryMonth.stream()
                    .map(row -> {
                        Map<String, Object> newMap = new LinkedHashMap<>();
                        newMap.put("month", row[0]);
                        newMap.put("total_profit", row[1]);
                        return newMap;
                    })
                    .collect(Collectors.toList());

            Map<String, Object> dataSent = new LinkedHashMap<>();
            dataSent.put("role", user.isAdmin());
            dataSent.put("sales", monthlyProfitObject);
            return dataSent;
        }

        // If not admin, just return monthly revenue

        List<Object[]> allMonthreveue = saleRepo.getMonthlyRevenueByUserId(user.getId());
        List<Map<String, Object>> monthlyRevenueObject = allMonthreveue.stream()
                .map(row -> {
                    Map<String, Object> newMap = new LinkedHashMap<>();
                    newMap.put("month", row[0]);
                    newMap.put("total_revenue", row[1]);
                    return newMap;
                })
                .collect(Collectors.toList());

        Map<String, Object> dataSent = new LinkedHashMap<>();
        dataSent.put("role", user.isAdmin());
        dataSent.put("sales", monthlyRevenueObject);
        return dataSent;
    }

    @GetMapping("/detail")
    private String saleDetail(@RequestParam("sale_id") String saleId, Model model, HttpSession sesison) {
        SessionUser user = (SessionUser) sesison.getAttribute("user");
        if(user == null) {
            return "redirect:/login";
        }

        if(user.isFirstLogin() && !user.isAdmin()) {
            return "redirect:/set_password";
        }

        if(saleId == null || saleId.isEmpty()) {
            return "redirect:/sale_history";
        }
//        System.out.println(saleId);

        Iterable<SaleDetail> saleDetails = saledetailRepo.getAllBySale_SaleId(saleId);

        Sale sale = saleRepo.findSaleBySaleId(saleId);
        model.addAttribute("saleDetails", saleDetails);
        model.addAttribute("sale", sale);
        model.addAttribute("user", user);
        System.out.println(saleDetails.toString());

        return "saleDetail";
    }

    @GetMapping("/filter")
    private String filterByDate(@RequestParam(name = "start_date", required = false) String start_date, @RequestParam(name = "end_date", required = false) String end_date, @RequestParam(name = "time", required = false) String time, HttpSession session, Model model) {

        SessionUser user = (SessionUser) session.getAttribute("user");
        if(user == null) {
            return "redirect:/login";
        }

        if(user.isFirstLogin() && !user.isAdmin()) {
            return "redirect:/set_password";
        }


        List<Sale> sales;
        int totalRevenue = 0;
        int totalQuantity = 0;
        long totalOrder = 0;
        if(user.isAdmin()) {
            LinkedHashMap<String, Object> saleData = saleService.getSaleDataByDate(start_date, end_date, time);
            sales = (List<Sale>) saleData.get("sales");
            totalRevenue = (int) saleData.get("totalRevenue");
            totalQuantity = (int) saleData.get("totalQuantity");
            totalOrder = (long) saleData.get("totalOrder");
        }
        else {
            int userId = user.getId();
            LinkedHashMap<String, Object> saleData = saleService.getSaleDataByDateAndUserId(start_date, end_date, time, userId);
            sales = (List<Sale>) saleData.get("sales");
            totalRevenue = (int) saleData.get("totalRevenue");
            totalQuantity = (int) saleData.get("totalQuantity");
            totalOrder = (long) saleData.get("totalOrder");

        }

        model.addAttribute("user", user);
        model.addAttribute("sales", sales);
        model.addAttribute("totalRevenue", totalRevenue);
        model.addAttribute("totalQuantity", totalQuantity);
        model.addAttribute("totalOrder", totalOrder);

        return "saleHistory";

    }

}
