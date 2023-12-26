package com.dkkhoa.possystem.controller;

import com.dkkhoa.possystem.model.products.Product;
import com.dkkhoa.possystem.model.products.ProductRepository;
import com.dkkhoa.possystem.model.products.ProductService;
import com.dkkhoa.possystem.model.users.SessionUser;
import com.dkkhoa.possystem.utils.RenameFileUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepo;

    @Autowired
    private ProductService productService;

    private static String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/src/main/upload/products";

    @Autowired
    private RenameFileUtil renameFileUtil;
    @GetMapping("")

    public String viewProducts(HttpSession session, Model model, HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");

        SessionUser userSession = (SessionUser) session.getAttribute("user");
        if(userSession == null) {
            return "redirect:/login";
        }
        if(userSession.isFirstLogin() && !userSession.isAdmin()) {
            return "redirect:/set_password";
        }

        Iterable<Product> products = productRepo.findAll();

        model.addAttribute("user", userSession);
        model.addAttribute("products", products);

        return "viewProducts";
    }


    @GetMapping("/delete/{id}")
    public String deleteProduct(HttpSession session, Model model, @PathVariable("id") int productId, HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");

        SessionUser userSession = (SessionUser) session.getAttribute("user");
        if(userSession == null) {
            return "redirect:/login";
        }
        if(userSession.isFirstLogin() && !userSession.isAdmin()) {
            return "redirect:/set_password";
        }

        boolean isDeleted = productService.deleteProductById(productId);
        if(isDeleted) {
            return "redirect:/products";
        }
        model.addAttribute("error", "Cannot delete product");
        return "error";

    }

    @GetMapping("/view/{id}")
    private String viewProduct(HttpSession session, Model model, @PathVariable("id") int productId, HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");
        SessionUser userSession = (SessionUser) session.getAttribute("user");


        if(userSession == null) {
            return "redirect:/login";
        }
        if(userSession.isFirstLogin() && !userSession.isAdmin()) {
            return "redirect:/set_password";
        }
        Product product = productRepo.findProductByProductId(productId);
        model.addAttribute("product", product);
        model.addAttribute("user", userSession);
        return "viewAndUpdateProduct";
    }

    @PostMapping("/update/{id}")
    private String updateProduct(HttpSession session, Model model, @PathVariable("id") int productId,
                              @RequestParam(name = "productName") String productName,
                              @RequestParam("importPrice") int importPrice,
                              @RequestParam("retailPrice") int retailPrice,
                              @RequestParam(name = "manufacturer", required = false) String manufacturer,
                              @RequestParam(name = "category", required = false) String category,
                              @RequestParam(name = "quantity") int quantity,
                              @RequestParam(name = "productImage", required = false) MultipartFile productImage) throws IOException {
        SessionUser userSession = (SessionUser) session.getAttribute("user");

        if(userSession == null) {
            return "redirect:/login";
        }
        if(userSession.isFirstLogin() && !userSession.isAdmin()) {
            return "redirect:/set_password";
        }


        Product product = productRepo.findProductByProductId(productId);
        String currentProductImage = product.getImage();

        if(productImage != null && !productImage.isEmpty()) {
            String newFilename = renameFileUtil.gerateNewFileName(productName, productImage, 0);
//            StringBuilder fileName = new StringBuilder();
            Path filePath = Paths.get(UPLOAD_DIRECTORY, newFilename);
//            fileName.append(productImage.getOriginalFilename());
            Files.write(filePath, productImage.getBytes());
            currentProductImage = newFilename;

        }


        boolean isUpdated = productService.updateProduct(productId, productName, importPrice, retailPrice, manufacturer, category, currentProductImage, quantity);
        if(isUpdated) {
            return "redirect:/products/view/" + productId;
        }
        model.addAttribute("error", "Unable to update product with id: " + productId);
        return "error";
    }

    @PostMapping("/add")
    private String addProduct(HttpSession session, Model model, @RequestParam(name = "productName") String productName,
                              @RequestParam("importPrice") int importPrice,
                              @RequestParam("retailPrice") int retailPrice,
                              @RequestParam(name = "manufacturer", required = false) String manufacturer,
                              @RequestParam(name = "category", required = false) String category,
                              @RequestParam(name = "quantity") int quantity,
                              @RequestParam(name = "productImage") MultipartFile productImage) throws IOException {
        SessionUser userSession = (SessionUser) session.getAttribute("user");

        if(userSession == null) {
            return "redirect:/login";
        }
        if(userSession.isFirstLogin() && !userSession.isAdmin()) {
            return "redirect:/set_password";
        }

        String newFilename = renameFileUtil.gerateNewFileName(productName, productImage, 0);

        Path filePath = Paths.get(UPLOAD_DIRECTORY, newFilename);
        Files.write(filePath, productImage.getBytes());

        boolean isAdded = productService.addProduct(productName, importPrice, retailPrice, manufacturer, category, newFilename, quantity);
        if(isAdded) {
            return "redirect:/products";
        }
        model.addAttribute("error", "Unable to add product");
        return "error";
    }


}
