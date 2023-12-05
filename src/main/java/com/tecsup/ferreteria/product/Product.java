package com.tecsup.ferreteria.product;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "product")
public class Product {
    
    @Id
    @GeneratedValue
    private Long productId;

    @NotNull(message = "El nombre del producto no puede ser nulo")
    @NotEmpty(message = "El nombre del producto no puede estar vacío")
    @Column(name = "name")
    private String name;

    @NotNull(message = "El precio del producto no puede ser nulo")
    @DecimalMin(value = "0.0", message = "El precio debe ser mayor que 0")
    @Column(name = "price")
    private Double price;

    @NotNull(message = "La categoría del producto no puede ser nula")
    @NotEmpty(message = "La categoría del producto no puede estar vacía")
    @Column(name = "category")
    private String category;

    @NotNull(message = "El stock del producto no puede ser nulo")
    @Min(value = 0, message = "El stock no puede ser negativo")
    @Column(name = "stock")
    private Integer stock;

    @Column(name = "description")
    private String description;

    @NotNull(message = "El estado del producto no puede ser nulo")
    @Column(name = "status")
    private Boolean status = true;
}
