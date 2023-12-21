package com.dkkhoa.possystem.controller;

import com.dkkhoa.possystem.model.saledetail.SaleDetailRepository;
import com.dkkhoa.possystem.model.sales.SaleRepository;
import com.dkkhoa.possystem.model.users.SessionUser;
import com.dkkhoa.possystem.model.users.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class SaleController {

    @Autowired
    SaleDetailRepository saledetailRepo;

    @Autowired
    SaleRepository saleRepo;


    @GetMapping("/sale_history")
    @ResponseBody
    public String sale_history(HttpSession session) {
        SessionUser user = (SessionUser) session.getAttribute("user");
        List<Object[]> allMonthProfit = saledetailRepo.getAllMonthProfit();
//        for (Object[] profitEachMonth: allMonthProfit) {
//            int month = (Integer) profitEachMonth[0];
//            long totalProfit = (Long) profitEachMonth[1];
//            System.out.println("Month: " + month + " - " + "Profit: " + totalProfit);
//
//        }

//        List<Map<String, Object>> processedProfitEveryMonth = allMonthProfit.stream()
//                .map(row -> {
//                    Map<String, Object> newMap = new LinkedHashMap<>();
//                    newMap.put("month", row[0]);
//                    newMap.put("profit", row[1]);
//                    return newMap;
//                })
//                .collect(Collectors.toList());
        List<Object[]> allMonthreveue = saleRepo.getMonthlyRevenueByUserId(user.getId());
        for (Object[] revenueEachMonth: allMonthreveue) {
            int month = (Integer) revenueEachMonth[0];
            long totalRevenue = (Long) revenueEachMonth[1];
            System.out.println("Month: " + month + " - " + "Revenue: " + totalRevenue);

        }

//        System.out.println(processedProfitEveryMonth);
        System.out.println();
        return "salehistory";
    }

//    @CrossOrigin
    @PostMapping("/sale_history/data")
    @ResponseBody
    public Map<String, Object> send_data(HttpSession session) {
        SessionUser user = (SessionUser) session.getAttribute("user");

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

}
