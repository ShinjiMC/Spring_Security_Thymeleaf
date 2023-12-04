package com.tecsup.ferreteria.security;

import lombok.RequiredArgsConstructor;

import java.util.Set;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tecsup.ferreteria.customer.Customer;
import com.tecsup.ferreteria.auth.*;
import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserAccountService implements
                UserDetailsService, AccountService {
        private final UserAccountRepository userAccountRepository;
        private final PasswordEncoder passwordEncoder;
        private final RoleRepository roleRepository;

        @Override
        public UserAccount createUserAccount(Customer customer) {
                UserAccount userAccount = new UserAccount(customer.getEmail(),
                                passwordEncoder.encode(customer.getPassword()))
                                .addRoles(Collections.singleton(createNewUserRole()))
                                .addCustomer(customer);
                Optional<UserAccount> userAccountByUsername = userAccountRepository
                                .findUserAccountByUsername(userAccount.getUsername());
                if (userAccountByUsername.isEmpty()) {
                        return userAccountRepository.save(userAccount);
                }
                return userAccountByUsername
                                .stream()
                                .findFirst()
                                .orElseThrow(() -> new RuntimeException("user account not found"));
        }

        private Role createNewUserRole() {
                Role role = new Role("USER")
                                .addAuthorities(Set.of(
                                                new Authority("customer:read"),
                                                new Authority("customer:write")));

                return roleRepository.save(role);
        }

        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                UserAccount userAccount = userAccountRepository
                                .findUserAccountByUsername(username)
                                .stream()
                                .findFirst()
                                .orElseThrow(() -> new UsernameNotFoundException("username not found"));
                return new UserAccountDetails(userAccount);
        }
}
