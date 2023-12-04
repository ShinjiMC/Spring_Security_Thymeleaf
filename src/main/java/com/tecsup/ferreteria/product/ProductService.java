package com.tecsup.ferreteria.product;

import java.util.List;

public interface ProductService {
    Product createProduct(Product product);

    Product getProductById(Long productId);

    Product updateProduct(Product product);

    void deleteProduct(Long productId);

    List<Product> getAllProducts(String keyWord);
}
