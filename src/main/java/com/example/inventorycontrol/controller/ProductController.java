package com.example.inventorycontrol.controller;

import com.example.inventorycontrol.dto.ProductDto;
import com.example.inventorycontrol.entity.Product;
import com.example.inventorycontrol.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public Page<Product> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return productService.getAllProducts(page, size);
    }

    @PostMapping("/import")
    public ResponseEntity<String> importProducts(@RequestParam("file") MultipartFile file) {
        productService.importProductsFromExcel(file);
        return ResponseEntity.status(HttpStatus.OK).body("Import successful");
    }
    @PostMapping("/create")
    public ResponseEntity<String> createProduct(@RequestBody ProductDto product) {
        productService.createProduct(product);
        return ResponseEntity.status(HttpStatus.OK).body("Product created successfully");
    }

}
