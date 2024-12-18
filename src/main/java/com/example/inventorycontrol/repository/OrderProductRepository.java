package com.example.inventorycontrol.repository;

import com.example.inventorycontrol.entity.OrderProduct;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderProductRepository extends JpaRepository<OrderProduct, UUID> {
    void deleteByProductId(UUID productId);
    void deleteByOrderId(UUID orderId);
}
