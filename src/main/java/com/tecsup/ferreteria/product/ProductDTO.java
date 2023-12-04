package com.tecsup.ferreteria.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    
    private Long productId;
    private String name;
    private Double price;
    private Integer stock;

}
