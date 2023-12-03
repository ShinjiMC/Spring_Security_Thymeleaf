package com.javawhizz.SpringSecurity.security;

import com.javawhizz.SpringSecurity.customer.Customer;

public interface AccountService {
    UserAccount createUserAccount(Customer customer);
}
