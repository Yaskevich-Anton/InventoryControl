package com.example.inventorycontrol.controller;

import com.example.inventorycontrol.dto.OrderDto;
import com.example.inventorycontrol.entity.Order;
import com.example.inventorycontrol.entity.enums.Status;
import com.example.inventorycontrol.repository.OrderRepository;
import com.example.inventorycontrol.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;
    private final OrderRepository orderRepository;

    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{id}")
    public Order getOrderById(@PathVariable UUID id) {
        return orderService.getOrderById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
    }
    @PostMapping("/setStatus")
    public void setStatusOrder(@RequestBody OrderDto orderDto) {
        orderService.setStatusOrder(orderDto);
    }

    @PostMapping("/add/{id}/{quality}")
    public void addProductToOrder(@PathVariable UUID id, @PathVariable Integer quality){
        orderService.addProductToOrder(id,quality);
    }
    @PutMapping("/close")
    public void closeOrder(@RequestBody Order order){
        orderService.closeOrder(order);
    }

    @DeleteMapping("/{id}")
    public void deleteOrder(@PathVariable UUID id) {
        orderService.deleteOrder(id);
    }

}
