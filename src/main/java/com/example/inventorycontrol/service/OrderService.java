package com.example.inventorycontrol.service;

import com.example.inventorycontrol.entity.CartItem;
import com.example.inventorycontrol.entity.Cart;
import com.example.inventorycontrol.entity.Order;
import com.example.inventorycontrol.entity.Product;
import com.example.inventorycontrol.entity.enums.Status;
import com.example.inventorycontrol.repository.OrderRepository;
import com.example.inventorycontrol.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class OrderService {

        private OrderRepository orderRepository;
        private ProductRepository productRepository;

        public void setStatusOrder(UUID uuid, Status statusOrder) {
            Optional<Order> optionalOrder = orderRepository.findById(uuid);
            if (optionalOrder.isPresent()) {
                Order order = optionalOrder.get();
                order.setStatus(statusOrder);

                if (statusOrder.equals(Status.COMPLETED)) {
                    for (Product product : order.getProducts()) {
                        // Уменьшаем количество продукта в базе данных
                        Optional<Product> optionalProduct = productRepository.findById(product.getId());
                        if (optionalProduct.isPresent()) {
                            Product dbProduct = optionalProduct.get();
                            int newQuantity = dbProduct.getQuantity() - order.getProducts()
                                    .stream()
                                    .filter(p -> p.getId().equals(product.getId()))
                                    .findFirst()
                                    .get()
                                    .getQuantity();
                            dbProduct.setQuantity(newQuantity);
                            productRepository.save(dbProduct);
                        }
                    }
                }

                orderRepository.save(order);
            } else {
                throw new IllegalArgumentException("Order not found");
            }
        }
        public Order createOrder(Cart cart, String phoneNumber, String description) {
            if (cart.getItems().isEmpty()) {
                throw new IllegalStateException("Cart is empty!");
            }

            Order order = Order.builder()
                    .id(UUID.randomUUID())
                    .price(cart.getTotalAmount())
                    .status(Status.PENDING)
                    .phoneNumber(phoneNumber)
                    .date(new Date())
                    .description(description)
                    .products(cart.getItems().stream().map(CartItem::getProduct).toList())
                    .build();

            return orderRepository.save(order);
        }
}


