package com.dkkhoa.possystem.model.saledetail;

import com.dkkhoa.possystem.model.products.Product;
import com.dkkhoa.possystem.model.sales.Sale;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "sale_details")
public class SaleDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sale_detail_id")
    private int saleDetailId;
    @Column(name = "quantity")
    private int quantity;
    @Column(name = "unit_price")
    private int unitPrice;

    @ManyToOne
    @JoinColumn(name = "sale_id")
    private Sale sale;
    @ManyToOne
    @JoinColumn (name = "product_id")
    private Product product;

}
