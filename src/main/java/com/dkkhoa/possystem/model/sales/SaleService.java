package com.dkkhoa.possystem.model.sales;

import com.dkkhoa.possystem.model.customers.Customer;
import com.dkkhoa.possystem.model.customers.CustomerRepository;
import com.dkkhoa.possystem.model.products.ProductSelected;
import com.dkkhoa.possystem.model.saledetail.SaleDetailService;
import com.dkkhoa.possystem.model.users.User;
import com.dkkhoa.possystem.model.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class SaleService {

    @Autowired
    SaleRepository saleRepo;

    @Autowired
    UserRepository userRepo;

    @Autowired
    CustomerRepository customerRepo;

    @Autowired
    SaleDetailService saledetailService;


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

    public LinkedHashMap<String, Object> getSaleDataByDate(String start_date, String end_date, String time) {
        try {
            LocalDate startDate = null;
            LocalDate endDate = null;

            LocalDate today = LocalDate.now();
            if((start_date != null && end_date != null) && (!start_date.isEmpty() && !end_date.isEmpty())) {
                startDate = LocalDate.parse(start_date);
                endDate = LocalDate.parse(end_date);
            }
            else if(start_date != null && !start_date.isEmpty()) {
                startDate = LocalDate.parse(start_date);
                endDate = today;
            }
            else if(end_date != null && !end_date.isEmpty()) {
                startDate = LocalDate.parse("2023-01-01");
                endDate = LocalDate.parse(end_date);
            }
            else {
                switch (time) {
                    case "Today":
                        startDate = today;
                        endDate = today;
                        break;
                    case "Yesterday":
                        startDate = today.minusDays(1);
                        endDate = today;
                        break;
                    case "Last_seven_days":
                        startDate = today.minusDays(7);
                        endDate = today;
                        break;
                    case "This_month":
                        startDate = today.withDayOfMonth(1);
                        endDate = today.withDayOfMonth(today.lengthOfMonth());
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid time: " + time);
                }
            }
            List<Sale> sales = saleRepo.findBySaleDateBetween(startDate, endDate);
            int totalRevenue = saleRepo.getTotalRevenueBySaleDateBetween(startDate, endDate);
            int totalQuantity = saleRepo.getTotalQuantityBySaleDateBetween(startDate, endDate);
            long totalOrder = saleRepo.countBySaleDateBetween(startDate, endDate);

            LinkedHashMap<String, Object> saleData = new LinkedHashMap<>();
            saleData.put("sales", sales);
            saleData.put("totalRevenue", totalRevenue);
            saleData.put("totalQuantity", totalQuantity);
            saleData.put("totalOrder", totalOrder);
            System.out.println(sales.toString());
            return saleData;
        }
        catch (Exception e) {
            System.out.println("An error occured" + e);
            return null;
        }
    }

    public LinkedHashMap<String, Object> getSaleDataByDateAndUserId(String start_date, String end_date, String time, int userId) {
        try {
            LocalDate startDate = null;
            LocalDate endDate = null;

            LocalDate today = LocalDate.now();
            if((start_date != null && !start_date.isEmpty()) && (end_date != null && !end_date.isEmpty())) {
                System.out.println("SaleService, dong 109");
                startDate = LocalDate.parse(start_date);
                endDate = LocalDate.parse(end_date);
            }
            else if(start_date != null && !start_date.isEmpty()) {
                System.out.println("SaleService, dong 114");
                startDate = LocalDate.parse(start_date);
                endDate = today;
            }
            else if(end_date != null && !end_date.isEmpty()) {
                System.out.println("SaleService, dont 119");
                startDate = LocalDate.parse("2023-01-01");
                endDate = LocalDate.parse(end_date);
            }
            else {
                switch (time) {
                    case "Today":
                        startDate = today;
                        endDate = today;
                        break;
                    case "Yesterday":
                        startDate = today.minusDays(1);
                        endDate = today;
                        break;
                    case "Last_seven_days":
                        startDate = today.minusDays(7);
                        endDate = today;
                        break;
                    case "This_month":
                        startDate = today.withDayOfMonth(1);
                        endDate = today.withDayOfMonth(today.lengthOfMonth());
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid time: " + time);
                }
            }
            List<Sale> sales = saleRepo.findSaleBySaleDateBetweenAndUserId(startDate, endDate, userId);
            int totalRevenue = saleRepo.getTotalRevenueByUserIdAndSaleDateBetween(startDate, endDate, userId);
            int totalQuantity = saleRepo.getTotalQuantityByUserIdAndSaleDateBetween(startDate, endDate, userId);
            long totalOrder = saleRepo.countByUserIdAndSaleDateBetwen(startDate, endDate, userId);
            System.out.println(sales.toString());

            LinkedHashMap<String, Object> saleData = new LinkedHashMap<>();
            saleData.put("sales", sales);
            saleData.put("totalRevenue", totalRevenue);
            saleData.put("totalQuantity", totalQuantity);
            saleData.put("totalOrder", totalOrder);
            return saleData;
        }
        catch (Exception e) {
            System.out.println("An error occured with user id: " + e);
            return null;
        }
    }




}
