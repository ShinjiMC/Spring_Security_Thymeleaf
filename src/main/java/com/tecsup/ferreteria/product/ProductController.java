package com.tecsup.ferreteria.product;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping("/newProduct")
    public String createProduct(@ModelAttribute("product") Product product) {
        productService.createProduct(product);
        return "redirect:/profile";
    }

    @GetMapping("/{id}")
    public Product getProductById(Long productId) {
        return productService.getProductById(productId);
    }

    @GetMapping("/agregateProduct")
    public String agregateProduct() {
        return "agregateproduct";
    }

}
