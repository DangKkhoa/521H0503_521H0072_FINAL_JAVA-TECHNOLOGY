package com.dkkhoa.possystem.model.products;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    @Autowired
    ProductRepository productRepo;

    public Product getProductById(int id) {
        return productRepo.findProductByProductId(id);
    }


}
