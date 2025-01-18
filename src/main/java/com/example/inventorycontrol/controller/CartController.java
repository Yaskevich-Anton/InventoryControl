package com.example.inventorycontrol.controller;

import com.example.inventorycontrol.entity.Cart;
import com.example.inventorycontrol.entity.Order;
import com.example.inventorycontrol.entity.Product;
import com.example.inventorycontrol.service.CartService;
import com.example.inventorycontrol.service.ProductService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    @GetMapping
    public Cart getCart(HttpSession session) {
        return cartService.getCart(session);
    }

    @PostMapping("/add")
    public void addProduct(HttpSession session, @RequestBody Product product, @RequestParam int quantity) {
        cartService.addProduct(session, product, quantity);
    }

    @DeleteMapping("/remove/{productId}")
    public void removeProduct(HttpSession session, @PathVariable UUID productId) {
        cartService.removeProduct(session, productId);
    }

    @DeleteMapping("/clear")
    public void clearCart(HttpSession session) {
        cartService.clearCart(session);
    }
    @PostMapping("/checkout")
    public Order checkout(HttpSession session, @RequestParam String phoneNumber, @RequestParam(required = false) String description) {
        return cartService.checkout(session, phoneNumber, description);
    }
}