package com.tecsup.ferreteria.security;

import com.tecsup.ferreteria.customer.Customer;

public interface AccountService {
    UserAccount createUserAccount(Customer customer);
}
