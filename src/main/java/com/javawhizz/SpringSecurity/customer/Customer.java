package com.javawhizz.SpringSecurity.customer;

import com.javawhizz.SpringSecurity.security.UserAccount;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "customer")
public class Customer {
    @Id
    @GeneratedValue
    private Long customerId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Transient
    private String password;

    @OneToOne(
            mappedBy = "customer",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    private UserAccount userAccount;
}
