package com.example.inventorycontrol.service;

import com.example.inventorycontrol.dto.ProductDto;
import com.example.inventorycontrol.entity.Product;
import com.example.inventorycontrol.repository.OrderProductRepository;
import com.example.inventorycontrol.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Function;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@Service
@Slf4j
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final OrderProductRepository orderProductRepository;
    public static final String PHOTO_DIRECTORY = System.getProperty("user.home") + "/Downloads/uploads/";

    public Page<Product> getAllProducts(int page, int size) {
        log.info("Get all products");
        return productRepository.findAll(PageRequest.of(page, size, Sort.by("name")));
    }

    public Product getProduct(UUID uuid) {
        log.info("Get product by uuid: {}", uuid);
        return productRepository.findById(uuid).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public Product getProductByName(String name) {
        log.info("Get product by name: {}", name);
        return productRepository.findByName(name).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public Product createProduct(ProductDto productDto) {
        log.info("Create product: {}", productDto);
        Product product = Product.builder()
                .name(productDto.getName())
                .measureUnit(productDto.getMeasureUnit())
                .description(productDto.getDescription())
                .price(productDto.getPrice())
                .country(productDto.getCountry())
                .type(productDto.getType())
                .quantity(productDto.getQuantity())
                .photoUrl(productDto.getPhotoUrl())
                .build();
        return productRepository.save(product);
    }

    public Product saveProduct(Product product) {
        log.info("Save product: {}", product);
        return productRepository.save(product);
    }

    @Transactional
    public void deleteProduct(UUID id) {
        // Сначала удаляем все связанные записи из order_product
        orderProductRepository.deleteByProductId(id);

        // Затем удаляем сам продукт
        productRepository.deleteById(id);
    }

    public String uploadPhoto(UUID id, MultipartFile file) {
        log.info("Saving picture for user ID: {}", id);
        Product product = getProduct(id);
        String photoUrl = photoFunction.apply(id, file);
        product.setPhotoUrl(photoUrl);
        productRepository.save(product);
        return photoUrl;
    }
    // переделать на url
    private final Function<String, String> fileExtension = filename -> Optional.of(filename).filter(name -> name.contains("."))
            .map(name -> "." + name.substring(filename.lastIndexOf(".") + 1)).orElse(".png");

    private final BiFunction<UUID, MultipartFile, String> photoFunction = (uuid, image) -> {
        String filename = uuid + fileExtension.apply(image.getOriginalFilename()); // создаём имя изображения через uuid и оригинальное имя изо
        try {
            Path fileStorageLocation = Paths.get(PHOTO_DIRECTORY).toAbsolutePath().normalize();
            if(!Files.exists(fileStorageLocation)) { Files.createDirectories(fileStorageLocation); }
            Files.copy(image.getInputStream(), fileStorageLocation.resolve(filename), REPLACE_EXISTING);
            return ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/contacts/image/" + filename).toUriString();
        }catch (Exception exception) {
            throw new RuntimeException("Unable to save image");
        }
    };

}
