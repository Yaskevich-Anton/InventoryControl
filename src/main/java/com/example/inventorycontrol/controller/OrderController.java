package com.example.inventorycontrol.controller;

import com.example.inventorycontrol.dto.OrderDto;
import com.example.inventorycontrol.entity.Order;
import com.example.inventorycontrol.entity.enums.Status;
import com.example.inventorycontrol.repository.OrderRepository;
import com.example.inventorycontrol.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;
    private final OrderRepository orderRepository;

    @PutMapping("/{uuid}/status")
    public ResponseEntity<String> updateOrderStatus(
            @PathVariable UUID uuid,
            @RequestParam Status statusOrder) {
        orderService.setStatusOrder(uuid, statusOrder);
        return ResponseEntity.ok("Статус заказа успешно обновлен.");
    }


}
