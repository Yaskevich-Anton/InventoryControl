package com.example.inventorycontrol.entity;

import com.example.inventorycontrol.entity.enums.MeasureUnit;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue
    private UUID id;
    private String name;
    private MeasureUnit measureUnit;
    private String description;
    private Double price;
    private String country;
    private String type;
    private Integer quantity;
    private String photoUrl;
}