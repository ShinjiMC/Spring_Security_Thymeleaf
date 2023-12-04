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
    private Integer quantity;

    public ProductDTO(Product product) {
        if(product == null) return;
        this.productId = product.getProductId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.stock = product.getStock();
    }
    
}
