package com.javawhizz.SpringSecurity.security;

import com.javawhizz.SpringSecurity.customer.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.Banner;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class UserAccountController {
    private final UserAccountRepository userAccountRepository;

    @GetMapping
    public String home(Model model){
        model.addAttribute("customer", new Customer());
        return "register";
    }

    @GetMapping("/login")
    public String signIn(){
        return "login";
    }

    @PreAuthorize("hasAuthority('customer:read')")
    @GetMapping("/profile")
    public String userProfile(Authentication authentication, Model model){
        UserAccount userAccount = userAccountRepository
                .findUserAccountByUsername(authentication.getName())
                .stream()
                .findFirst()
                .orElseThrow(() -> new UsernameNotFoundException("username not found"));
        model.addAttribute("customer", userAccount.getCustomer());
        return "profile";
    }

}
