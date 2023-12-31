package com.tecsup.ferreteria.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tecsup.ferreteria.auth.Role;
import com.tecsup.ferreteria.customer.Customer;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class UserAccount {
    @Id
    @GeneratedValue
    private Long userAccountId;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "userAccount", cascade = {
            CascadeType.MERGE,
            CascadeType.DETACH,
            CascadeType.REFRESH,
            CascadeType.PERSIST
    })
    private Set<Role> roles = new HashSet<>();

    @OneToOne(fetch = FetchType.EAGER, cascade = {
            CascadeType.MERGE,
            CascadeType.DETACH,
            CascadeType.REFRESH,
            CascadeType.PERSIST
    })
    @JoinColumn(name = "customer_id")
    @JsonIgnore
    private Customer customer;

    private boolean isAccountNonExpired;
    private boolean isAccountNonLocked;
    private boolean isCredentialsNonExpired;
    private boolean isEnabled;

    public UserAccount(String username, String password) {
        this.username = username;
        this.password = password;
        this.isAccountNonLocked = true;
        this.isAccountNonExpired = true;
        this.isCredentialsNonExpired = true;
        this.isEnabled = true;
    }

    public UserAccount addRoles(Set<Role> newRoles) {
        this.roles.addAll(newRoles);
        for (Role role : newRoles) {
            if (role != null) {
                role.setUserAccount(this);
            }
        }
        return this;
    }

    public UserAccount addCustomer(Customer customer) {
        if (customer != null) {
            this.customer = customer;
            customer.setUserAccount(this);
        }
        return this;
    }
}
