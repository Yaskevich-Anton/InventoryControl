package com.example.inventorycontrol.entity;

import com.example.inventorycontrol.entity.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue
    private UUID id;
    private String username;
    private String password;
    private String country;
    private String phoneNumber;
    private String email;
    @Enumerated(EnumType.STRING)
    private Role role;

}
