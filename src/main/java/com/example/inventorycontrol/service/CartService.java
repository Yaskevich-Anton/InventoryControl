package com.example.inventorycontrol.service;

import com.example.inventorycontrol.entity.Cart;
import com.example.inventorycontrol.entity.CartItem;
import com.example.inventorycontrol.entity.Order;
import com.example.inventorycontrol.entity.Product;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartService {

    private static final String CART_SESSION_KEY = "cart";
    private final OrderService orderService;

    public Cart getCart(HttpSession session) {
        Cart cart = (Cart) session.getAttribute(CART_SESSION_KEY);
        if (cart == null) {
            cart = new Cart();
            session.setAttribute(CART_SESSION_KEY, cart);
        }
        return cart;
    }

    public void addProduct(HttpSession session, Product product, int quantity) {
        Cart cart = getCart(session);
        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst();

        if (existingItem.isPresent()) {
            existingItem.get().setQuantity(existingItem.get().getQuantity() + quantity);
        } else {
            cart.getItems().add(new CartItem(product, quantity));
        }
        updateTotalAmount(cart);
        session.setAttribute(CART_SESSION_KEY, cart);
    }

    public void removeProduct(HttpSession session, UUID productId) {
        Cart cart = getCart(session);
        cart.getItems().removeIf(item -> item.getProduct().getId().equals(productId));
        updateTotalAmount(cart);
        session.setAttribute(CART_SESSION_KEY, cart);
    }

    public void clearCart(HttpSession session) {
        Cart cart = getCart(session);
        cart.getItems().clear();
        cart.setTotalAmount(0);
        session.setAttribute(CART_SESSION_KEY, cart);
    }

    private void updateTotalAmount(Cart cart) {
        double total = cart.getItems().stream()
                .mapToDouble(item -> item.getProduct().getPrice_sale() * item.getQuantity())
                .sum();
        cart.setTotalAmount(total);
    }
    public Order checkout(HttpSession session, String phoneNumber, String description) {
        Cart cart = getCart(session);
        Order order = orderService.createOrder(cart, phoneNumber, description);
        clearCart(session);
        return order;
    }
}