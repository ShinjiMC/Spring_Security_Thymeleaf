package com.tecsup.ferreteria.customer;

import com.tecsup.ferreteria.security.UserAccount;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
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
    @NotEmpty(message = "El nombre no puede estar vacio")
    @Size(min = 2, message = "El nombre debe tener al menos 2 caracteres")
    private String firstName;

    @Column(name = "last_name")
    @NotEmpty(message = "El apellido no puede estar vacio")
    @Size(min = 2, message = "El apellido debe tener al menos 2 caracteres")
    private String lastName;

    @Column(name = "email")
    @NotEmpty(message = "El email no puede estar vacio")
    @Email(message = "El email debe ser valido")
    private String email;

    @Transient
    @NotEmpty(message = "La contraseña no puede estar vacia")
    private String password;

    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private UserAccount userAccount;
}
