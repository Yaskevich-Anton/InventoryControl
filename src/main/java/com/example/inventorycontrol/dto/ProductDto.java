package com.example.inventorycontrol.dto;

import com.example.inventorycontrol.entity.enums.MeasureUnit;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class  ProductDto {
    private String name;
    private String description;
    private Double price_purchase;
    private Double price_sale;
    private Integer quantity;
    private String photoUrl;
}
