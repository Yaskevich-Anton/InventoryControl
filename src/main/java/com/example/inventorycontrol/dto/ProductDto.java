package com.example.inventorycontrol.dto;

import com.example.inventorycontrol.entity.enums.MeasureUnit;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class  ProductDto {
    private String name;
    private MeasureUnit measureUnit;
    private String description;
    private Double price;
    private String country;
    private String type;
    private Integer quantity;
    private String photoUrl;
}
