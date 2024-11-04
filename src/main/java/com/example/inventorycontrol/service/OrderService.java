package com.example.inventorycontrol.service;

import com.example.inventorycontrol.dto.UserDto;
import com.example.inventorycontrol.entity.Order;
import com.example.inventorycontrol.entity.Product;
import com.example.inventorycontrol.entity.User;
import com.example.inventorycontrol.entity.enums.Role;
import com.example.inventorycontrol.entity.enums.Status;
import com.example.inventorycontrol.exception.ApplicationException;
import com.example.inventorycontrol.mapper.UserMapper;
import com.example.inventorycontrol.repository.OrderRepository;
import com.example.inventorycontrol.repository.ProductRepository;
import com.example.inventorycontrol.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final SecurityContextResolverService securityContextResolverService;
    private final UserMapper userMapper;

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Optional<Order> getOrderById(UUID id) {
        return orderRepository.findById(id);
    }

    public Order getCurrentOrders(User user) {
        return orderRepository.findByUser(user);
    }


    public Order createOrder() {
        UserDto userDto = securityContextResolverService.getUserFromAuth();
        User user = userMapper.toUser(userDto);
        return Order.builder()
                .status(Status.WAITING)
                .date(new Date())
                .userId(user)
                .products(new HashMap<>())
                .build();
    }

    public void addProductToOrder(UUID productId,Integer quality) {

       UserDto user = securityContextResolverService.getUserFromAuth();
        Optional<Order> orderFromDB = orderRepository.findOrderByUser(user.getId());
        Optional<Product> productOptional = productRepository.findById(productId);
        if(productOptional.isEmpty()){
            throw new ApplicationException(HttpStatus.NOT_FOUND,"Product not found");
        }
        Product product = productOptional.get();

        if(orderFromDB.isEmpty()){
            Order order = createOrder();
            order.setProducts(product,quality);
            orderRepository.save(order);
        }
        Map<Product,Integer> products = new HashMap<>();
        products.put(product,quality);
        Order changedOrder = orderFromDB.get();
        changedOrder.setProducts(products);
        orderRepository.save(changedOrder);

    }


    public void deleteOrder(UUID id) {
        orderRepository.deleteById(id);
    }

    public void closeOrder(Order order) {
        UserDto userDto = securityContextResolverService.getUserFromAuth();
        Role role = userDto.getRole();
        if(role.equals(Role.USER)){

        }

    }
}
