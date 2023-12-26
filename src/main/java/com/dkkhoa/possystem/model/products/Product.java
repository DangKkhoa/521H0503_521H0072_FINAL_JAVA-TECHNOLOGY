package com.dkkhoa.possystem.model.products;

import com.dkkhoa.possystem.model.saledetail.SaleDetail;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

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
    @Column(name = "quantity_in_stock")
    private int quantityInStock;

//    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
//    private List<SaleDetail> salesTransactions;

}
