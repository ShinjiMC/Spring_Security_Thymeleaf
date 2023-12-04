package com.tecsup.ferreteria.product;

import java.util.List;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    @Override
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Product getProductById(Long productId) {
        return productRepository.findById(productId).orElse(null);
    }

    @Override
    public void deleteProduct(Long productId) {
        productRepository.deleteById(productId);
    }

    @Override
    public List<Product> getAllProducts(String keyWord) {
        if (keyWord != null) {
            return productRepository.findAll(keyWord);
        }
        return productRepository.findAll();
    }

    @Override
    public ProductDTO getProductDTOById(Long productId) {
        if(productId == null)
            System.out.println("-----> productId is null");
        return new ProductDTO(productRepository.findById(productId).orElse(null));
    }
}
