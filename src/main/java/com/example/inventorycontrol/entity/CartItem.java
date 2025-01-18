package com.example.inventorycontrol.entity;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItem {
    private Product product;
    private int quantity;
}
