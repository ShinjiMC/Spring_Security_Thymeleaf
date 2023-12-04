package com.tecsup.ferreteria.security;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tecsup.ferreteria.customer.Customer;
import com.tecsup.ferreteria.product.Product;
import com.tecsup.ferreteria.product.ProductService;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class UserAccountController {
    private final UserAccountRepository userAccountRepository;
    private final ProductService productService;

    @GetMapping
    public String home(Model model) {
        model.addAttribute("customer", new Customer());
        return "register";
    }

    @GetMapping("/login")
    public String signIn() {
        return "login";
    }

    @GetMapping("/products")
    public String viewProducts(Model model, @Param("keyword") String keyword) {
        List<Product> listaProductos = productService.getAllProducts(keyword);
        model.addAttribute("listProducts", listaProductos);
        model.addAttribute("keyword", keyword);
        return "products";
    }

    @PreAuthorize("hasAuthority('customer:read')")
    @GetMapping("/profile")
    public String userProfile(Authentication authentication, Model model) {
        UserAccount userAccount = userAccountRepository
                .findUserAccountByUsername(authentication.getName())
                .stream()
                .findFirst()
                .orElseThrow(() -> new UsernameNotFoundException("username not found"));
        model.addAttribute("customer", userAccount.getCustomer());
        return "profile";
    }

}
