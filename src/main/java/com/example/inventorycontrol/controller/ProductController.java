package com.example.inventorycontrol.controller;

import com.example.inventorycontrol.dto.ProductDto;
import com.example.inventorycontrol.entity.Product;
import com.example.inventorycontrol.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {
    private final ProductService productService;

    @GetMapping("")
    public ResponseEntity<List<Product>> getProducts(){
        return productService.getAllProducts();
    }

    @PostMapping("/create")
    public ResponseEntity<Product> createProduct(@RequestBody ProductDto productDto) {
        Product createdProduct = productService.createProduct(productDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }
    @PostMapping("/add")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable UUID id){
        return productService.deleteProduct(id);
    }
}
