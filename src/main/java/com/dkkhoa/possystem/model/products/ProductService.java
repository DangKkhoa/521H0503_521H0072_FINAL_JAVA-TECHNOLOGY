package com.dkkhoa.possystem.model.products;

import com.dkkhoa.possystem.model.saledetail.SaleDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ProductService {
    @Autowired
    ProductRepository productRepo;

    @Autowired
    private SaleDetailRepository saleDetailRepo;

    public Product getProductById(int id) {
        return productRepo.findProductByProductId(id);
    }


    public Product updateQuantityInStock(int productId, int quantitySold) {
        System.out.println("Product id: " + productId);
        Product product = productRepo.findProductByProductId(productId);
        int newQuantityInStock = product.getQuantityInStock() - quantitySold;
        product.setQuantityInStock(newQuantityInStock);
        productRepo.save(product);
        return product;
    }


    public boolean deleteProductById(int productId) {
        int productSold = saleDetailRepo.countByProduct_ProductId(productId);
        System.out.println(productSold);
        if(productSold > 0) {
            return false;
        }
        productRepo.deleteById(productId);
        return true;
    }


    public boolean addProduct(String productName, int importPrice, int retailPrice, String manufacturer, String category, String productImage, int quantity) {
        try {
            Product product = new Product();
            product.setProductName(productName);
            product.setImportPrice(importPrice);
            product.setRetailPrice(retailPrice);
            product.setManufacturer(manufacturer);
            product.setCategory(category);
            product.setImage(productImage);
            product.setQuantityInStock(quantity);
            product.setCreationDate(LocalDate.now());
            productRepo.save(product);
            return true;
        }
        catch (Exception e) {
            System.out.println("ERROR WHILE ADDING NEW PRODUCT: " + e);
            return false;
        }
    }

    public boolean updateProduct(int productId, String productName, int importPrice, int retailPrice, String manufacturer, String category, String productImage, int quantity) {
        try {
            Product product = productRepo.findProductByProductId(productId);
            product.setProductName(productName);
            product.setImportPrice(importPrice);
            product.setRetailPrice(retailPrice);
            product.setManufacturer(manufacturer);
            product.setCategory(category);
            product.setImage(productImage);
            product.setQuantityInStock(quantity);
            product.setCreationDate(LocalDate.now());
            productRepo.save(product);
            return true;
        }
        catch (Exception e) {
            System.out.println("ERROR WHILE UPDATING PRODUCT WITH ID " + productId + ": " + e);
            return false;
        }
    }


}
