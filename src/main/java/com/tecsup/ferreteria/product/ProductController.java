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
import com.tecsup.ferreteria.boleta.*;

@Controller
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final BoletaService boletaService;
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
        for (Map.Entry<String, String> entry : params.entrySet()) {
            Long productId = Long.parseLong(entry.getKey());
            int quantity = (entry.getValue() == "") ? 0 : Integer.parseInt(entry.getValue());
            if (quantity != 0) {
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

    @PostMapping("/printBoleta")
    public ModelAndView generarBoleta(@RequestParam("productIds") List<Long> productIds,
            @RequestParam("quantities") List<Integer> quantities, Model model) {
        Double precioTotal = 0.0;
        List<ProductoDetalle> detalles = new ArrayList<>();
        for (int i = 0; i < productIds.size(); i++) {
            Long productId = productIds.get(i);
            Integer quantity = quantities.get(i);
            Product product = productService.getProductById(productId);

            if (product != null && quantity <= product.getStock() && quantity != null) {
                ProductoDetalle detalle = new ProductoDetalle();
                detalle.setNombre(product.getName());
                detalle.setPrecio(product.getPrice());
                detalle.setCantidad(quantity);
                Double productTotalPrice = product.getPrice() * quantity;
                precioTotal += productTotalPrice;
                detalles.add(detalle);
                product.setStock(product.getStock() - quantity);
                productService.saveProduct(product);
            }
        }
        Boleta boleta = new Boleta();
        boleta.setDetalles(detalles);
        boleta.setPrecioTotal(precioTotal);
        boletaService.saveBoleta(boleta);

        return new ModelAndView("redirect:/products/generarPDF/" + boleta.getId());
    }
}