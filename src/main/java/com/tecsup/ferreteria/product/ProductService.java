package com.tecsup.ferreteria.product;

import java.util.List;

public interface ProductService {
    Product saveProduct(Product product);

    Product getProductById(Long productId);

    void deleteProduct(Long productId);

    List<Product> getAllProducts();
}
