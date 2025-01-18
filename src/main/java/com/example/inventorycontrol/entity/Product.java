package com.example.inventorycontrol.entity;

import com.example.inventorycontrol.entity.enums.MeasureUnit;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue
    private UUID id;

    private String name;
    private String description;
    private Double price_purchase;
    private Double price_sale;
    private Integer quantity;
    private String photoUrl;
    private String type;
    @ManyToMany(mappedBy = "products")
    private List<Order> orders = new ArrayList<>();
}
