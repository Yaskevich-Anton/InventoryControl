package com.example.inventorycontrol.entity;

import com.example.inventorycontrol.entity.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.redis.core.RedisHash;

import java.util.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "orders")
public class Order {
        @Id
        @GeneratedValue
        private UUID id;
        @ManyToOne
        @JoinColumn(name = "user_id", nullable = false)
        private User userId;
        private Date date;
        private Status status;
        @ElementCollection
        @CollectionTable(name = "order_product", joinColumns = @JoinColumn(name = "order_id"))
        @MapKeyJoinColumn(name = "product_id")
        @Column(name = "quantity")
        private Map<Product, Integer> products = new HashMap<>();

    public void setProducts(Product product, int quantity) {
        if (quantity <= 0) {
            products.remove(product);
        } else {
            products.put(product, quantity);
        }
    }

}
