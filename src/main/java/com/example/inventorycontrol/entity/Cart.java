package com.example.inventorycontrol.entity;

import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cart {

    private List<CartItem> items = new ArrayList<>();
    private double totalAmount;

}
