package com.example.inventorycontrol.repository;

import com.example.inventorycontrol.entity.Order;
import com.example.inventorycontrol.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
//    @Query("SELECT o FROM Order o WHERE o.user_id = :user")
//    Order findByUser(@Param("user") User user);

    @Query(nativeQuery = true, value = "SELECT * FROM orders WHERE user_id = :uuid")
    Optional<Order> findOrderByUser(@Param("uuid") UUID uuid);

    @Query(nativeQuery = true, value = "SELECT * FROM orders WHERE user_id = :uuid")
    List<Order> findOrdersByUser(@Param("uuid") UUID uuid);
}
