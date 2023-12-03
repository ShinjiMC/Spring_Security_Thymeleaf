package com.tecsup.ferreteria.customer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.tecsup.ferreteria.security.UserAccountService;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final UserAccountService userAccountService;

    @Override
    public Customer createCustomer(Customer customer) {
        Customer theCustomer = customerRepository.save(customer);
        userAccountService.createUserAccount(theCustomer);
        return theCustomer;
    }
}
