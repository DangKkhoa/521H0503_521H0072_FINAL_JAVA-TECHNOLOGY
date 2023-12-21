package com.dkkhoa.possystem.model.products;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class ProductSelected extends Product{
    private int id;
    private String name;
    private int price;
    private int quantity;
}
