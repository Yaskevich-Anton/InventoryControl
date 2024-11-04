package com.example.inventorycontrol.service;

import com.example.inventorycontrol.dto.ProductDto;
import com.example.inventorycontrol.entity.Product;
import com.example.inventorycontrol.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productRepository.findAll());
    }
    public Optional<Product> getProductById(UUID uuid){
        return productRepository.findById(uuid);
    }
    public Optional<Product> getProductByName(String name){
        return productRepository.findByName(name);
    }

    public Product createProduct(ProductDto productDto) {
        Product product = Product.builder()
                .name(productDto.getName())
                .measureUnit(productDto.getMeasureUnit())
                .description(productDto.getDescription())
                .price(productDto.getPrice())
                .country(productDto.getCountry())
                .type(productDto.getType())
                .quantity(productDto.getQuantity())
                .build();

        return productRepository.save(product);
    }
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public ResponseEntity<?> deleteProduct(UUID id) {
        productRepository.deleteById(id);
        return ResponseEntity.ok("Succesfully");
    }

}
