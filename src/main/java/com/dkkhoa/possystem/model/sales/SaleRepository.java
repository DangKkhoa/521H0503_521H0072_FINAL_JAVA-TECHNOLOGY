package com.dkkhoa.possystem.model.sales;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface SaleRepository extends CrudRepository<Sale, Integer> {
    @Query("SELECT COALESCE(SUM(totalPrice), 0) FROM Sale")
    int getTotalRevenue();


    @Query("SELECT MONTH(s.saleDate), SUM(s.totalPrice) FROM Sale s WHERE s.user.userId = :userId GROUP BY MONTH(s.saleDate) ORDER BY MONTH(s.saleDate)")
    List<Object[]> getMonthlyRevenueByUserId(@Param("userId") int userId);

    Sale findSaleBySaleId(String saleId);

}
