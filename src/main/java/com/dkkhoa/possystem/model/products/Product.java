package com.dkkhoa.possystem.model.products;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private int productId;
    @Column(name = "product_name")
    private String productName;
    @Column(name = "import_price")
    private int importPrice;
    @Column(name = "retail_price")
    private int retailPrice;
    private String manufacturer;
    private String category;
    private String image;
    @Column(name = "creation_date")
    private LocalDate creationDate;

}
