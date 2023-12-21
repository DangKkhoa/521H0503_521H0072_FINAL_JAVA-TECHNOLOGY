package com.dkkhoa.possystem.model.saledetail;

import com.dkkhoa.possystem.model.products.Product;
import com.dkkhoa.possystem.model.products.ProductRepository;
import com.dkkhoa.possystem.model.products.ProductSelected;
import com.dkkhoa.possystem.model.sales.Sale;
import com.dkkhoa.possystem.model.sales.SaleRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@AllArgsConstructor
public class SaleDetailService {

    @Autowired
    private SaleDetailRepository saledetailRepo;

    @Autowired
    private ProductRepository productRepo;

    @Autowired
    private SaleRepository saleRepo;

    public ProductSelected format(Map<String, Object> data) {
        int id = Integer.parseInt(data.get("id").toString()) ;
        String name = data.get("name").toString();
        int price = Integer.parseInt(data.get("price").toString());
        int quantity = Integer.parseInt(data.get("quantity").toString());
        System.out.println("Product id: " + id + " - " + "Product name: " + name + " - " + "Product price: " + price + " - " + "Product quantity: " + quantity);
        return new ProductSelected(id, name, price, quantity);
    }

    public SaleDetail saledetailAdd(ProductSelected product, String saleId) {
        try {
            SaleDetail detail = new SaleDetail();
            detail.setQuantity(product.getQuantity());
            detail.setUnitPrice(product.getPrice());
            detail.setSale(saleRepo.findSaleBySaleId(saleId));
            detail.setProduct(productRepo.findProductByProductId(product.getId()));
            saledetailRepo.save(detail);
            return detail;
        }
        catch (Exception e) {
            System.out.println("Error occured while adding sale detail to db: " + e);
            return null;
        }
    }
}
