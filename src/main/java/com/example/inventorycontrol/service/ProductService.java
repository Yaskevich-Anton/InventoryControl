package com.example.inventorycontrol.service;

import com.example.inventorycontrol.dto.ProductDto;
import com.example.inventorycontrol.entity.Product;
import com.example.inventorycontrol.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public Page<Product> getAllProducts(int page, int size) {
        log.info("Getting all products, page: {}, size: {}", page, size);
        Page<Product> products = productRepository.findAll(PageRequest.of(page, size, Sort.by("name")));
        log.info("Number of products retrieved: {}", products.getNumberOfElements());
        return products;
    }

    public Product getProduct(UUID uuid) {
        log.info("Get product by uuid: {}", uuid);
        return productRepository.findById(uuid).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public Product getProductByName(String name) {
        log.info("Get product by name: {}", name);
        return productRepository.findByName(name).orElseThrow(() -> new RuntimeException("Product not found"));
    }


    public Product saveProduct(Product product) {
        log.info("Save product: {}", product);
        return productRepository.save(product);
    }


    public Product createProduct(ProductDto productDto) {
    log.info("Create product: {}", productDto);
    Product product = Product.builder()
            .name(productDto.getName())
            .description(productDto.getDescription())
            .price_sale(productDto.getPrice_sale())
            .photoUrl(productDto.getPhotoUrl())
            .build();
    return productRepository.save(product);
    }

    public void importProductsFromExcel(MultipartFile file){
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                String name = row.getCell(1).getStringCellValue();

                double price = 0.0;
                if (row.getCell(2) == null || row.getCell(2).getCellType() != CellType.NUMERIC) {
                    log.info("Skip the line: incorrect price format for the product: " + name);
                    continue;
                } else {
                    price = row.getCell(2).getNumericCellValue();
                }

                int quantity = 0;
                if (row.getCell(3) == null || row.getCell(3).getCellType() != CellType.NUMERIC) {
                   log.info("Skip the line: incorrect quantity format for the product: " + name);
                    continue;
                } else {
                    quantity = (int) row.getCell(3).getNumericCellValue();
                }

                Optional<Product> existingProductOpt = productRepository.findByName(name);

                if (existingProductOpt.isPresent()) {
                    Product existingProduct = existingProductOpt.get();
                    existingProduct.setQuantity(existingProduct.getQuantity() + quantity);
                    productRepository.save(existingProduct);
                } else {
                    Product product = new Product();
                    product.setName(name);
                    product.setPrice_purchase(price);
                    product.setQuantity(quantity);
                    productRepository.save(product);

                }
            }
        }  catch (IOException e) {
            throw new RuntimeException("Error reading file", e);
        }
    }


}
