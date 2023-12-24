package com.dkkhoa.possystem.model.saledetail;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
public interface SaleDetailRepository extends CrudRepository<SaleDetail, Long> {
    @Query("SELECT NEW com.dkkhoa.possystem.model.saledetail.TopFiveProducts (p.productName, SUM(sd.quantity)) " + "FROM SaleDetail sd " + "INNER JOIN sd.product p " + "GROUP BY p.productName " + "ORDER BY SUM(sd.quantity) DESC LIMIT 5")
    List<TopFiveProducts> findTop5Products();


    @Query("SELECT MONTH(s.saleDate) as month, " +
            "SUM(sd.unitPrice * sd.quantity - p.importPrice - sd.quantity) as totalQuantity " +
            "FROM SaleDetail sd " +
            "INNER JOIN  sd.sale s INNER JOIN sd.product p " +
            "GROUP BY month " +
            "ORDER BY MONTH(s.saleDate)"
    )
    List<Object[]> getAllMonthProfit();

//    @Query("SELECT sd FROM SaleDetail sd WHERE sd.sale.saleId = :saleId")
    Iterable<SaleDetail> getAllBySale_SaleId(String saleId);

}
