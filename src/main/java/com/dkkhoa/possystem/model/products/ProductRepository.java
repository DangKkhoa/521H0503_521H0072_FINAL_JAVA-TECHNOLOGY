package com.dkkhoa.possystem.model.products;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends CrudRepository<Product, Integer> {
    Product findProductByProductId(int id);

    @Query("SELECT p FROM Product p WHERE p.category LIKE :category%")
    Iterable<Product> findProductsByCategory(@Param("category") String category);

    Iterable<Product> findProductsByCategoryOrProductNameContains(String category, String name);

}
