package com.tecsup.ferreteria.product;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.ui.Model;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping("/agregateProduct")
    public String agregateProduct(Model model) {
        model.addAttribute("product", new Product());
        return "agregateProduct";
    }

    @PostMapping("/newProduct")
    public String createProduct(@ModelAttribute("product") Product product) {
        productService.saveProduct(product);
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

    @GetMapping
    public String viewProducts(Model model, @Param("keyword") String keyword) {
        List<Product> listaProductos = productService.getAllProducts(keyword);
        model.addAttribute("listProducts", listaProductos);
        model.addAttribute("keyword", keyword);
        return "products";
    }

    @GetMapping("/chooseProducts")
    public String viewChooseProducts(Model model, @Param("keyword") String keyword) {
        List<Product> listaProductos = productService.getAllProducts(keyword);
        model.addAttribute("listProducts", listaProductos);
        model.addAttribute("keyword", keyword);
        return "chooseProducts";
    }

    @PostMapping("/generarBoleta")
    public String generarBoleta(@RequestParam Map<String, String> params, Model model) {
        List<ProductDTO> selectedProducts = new ArrayList<>();
        // Itera sobre los parámetros del formulario para identificar los productos seleccionados
        for (Map.Entry<String, String> entry : params.entrySet()) {

            Long productId = Long.parseLong(entry.getKey());
            
            int quantity = (entry.getValue()=="") ? 0 : Integer.parseInt(entry.getValue());

            if(quantity != 0){
                ProductDTO product = productService.getProductDTOById(productId);
                
                if (product != null && quantity <= product.getStock()) {
                    product.setQuantity(quantity);
                    selectedProducts.add(product);
                }
            }

        }

        Double precioTotal = selectedProducts.stream().mapToDouble(p -> p.getPrice() * p.getQuantity()).sum();
        
        model.addAttribute("selectedProducts", selectedProducts);
        model.addAttribute("precioTotal", precioTotal);

        return "descargarBoleta";
    }

}
