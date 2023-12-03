package com.javawhizz.SpringSecurity.customer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping("/register")
    public String createCustomer(@ModelAttribute("customer")
                                 Customer customer){
        customerService.createCustomer(customer);
        return "redirect:/profile";
    }
}
