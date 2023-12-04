package com.tecsup.ferreteria.product;

public interface ProductService {
    Product createProduct(Product product);

    Product getProductById(Long productId);
}
