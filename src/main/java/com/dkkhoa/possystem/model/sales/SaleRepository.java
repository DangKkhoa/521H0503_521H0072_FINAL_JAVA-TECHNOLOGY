package com.dkkhoa.possystem.model.sales;

import com.dkkhoa.possystem.model.users.User;
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

    @Query("SELECT COALESCE(SUM(totalQuantity), 0) FROM Sale")
    int getTotalQuantity();

    @Query("SELECT COUNT(s) FROM Sale s WHERE s.user.userId = :userId")
    long countSaleByUserId(@Param("userId") int userId);

    @Query("SELECT COALESCE(SUM(s.totalPrice), 0) FROM Sale s WHERE s.user.userId = :userId")
    int getTotalRevenueByUserId(@Param("userId") int userId);

    @Query("SELECT COALESCE(SUM(s.totalQuantity), 0) FROM Sale s WHERE s.user.userId = :userId")
    int getTotalQuantityByUserId(@Param("userId") int userId);



    @Query("SELECT MONTH(s.saleDate), SUM(s.totalPrice) FROM Sale s WHERE s.user.userId = :userId GROUP BY MONTH(s.saleDate) ORDER BY MONTH(s.saleDate)")
    List<Object[]> getMonthlyRevenueByUserId(@Param("userId") int userId);

    Sale findSaleBySaleId(String saleId);

    @Query("SELECT s FROM Sale s WHERE s.user.userId = :userId ORDER BY s.saleDate")
    Iterable<Sale> getSaleByUserId(@Param("userId") int userId);





}
