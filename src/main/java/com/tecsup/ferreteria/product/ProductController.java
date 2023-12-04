package com.tecsup.ferreteria.product;

import java.util.List;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.ui.Model;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping("/")
    public String viewProducts(Model model, @Param("keyword") String keyword) {
        List<Product> listaProductos = productService.getAllProducts(keyword);
        model.addAttribute("listProducts", listaProductos);
        model.addAttribute("keyword", keyword);
        return "products";
    }

    @PostMapping("/newProduct")
    public String createProduct(@ModelAttribute("product") Product product) {
        productService.createProduct(product);
        return "redirect:/products";
    }

    @GetMapping("/{id}")
    public Product getProductById(Long productId) {
        return productService.getProductById(productId);
    }

    @GetMapping("/edit/{id}")
    public ModelAndView editProduct(@PathVariable(name = "id") Long productId) {
        ModelAndView mav = new ModelAndView("editProduct");
        Product product = productService.getProductById(productId);
        mav.addObject("product", product);
        return mav;
    }

    // Eliminar
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable(name = "id") Long productId) {
        productService.deleteProduct(productId);
        return "redirect:/products";
    }
}
