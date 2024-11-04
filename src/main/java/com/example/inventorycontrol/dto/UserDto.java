package com.example.inventorycontrol.dto;

import com.example.inventorycontrol.entity.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class UserDto {

    private UUID id;
    private String username;
    private String password;
    private String country;
    private String phoneNumber;
    private String email;
    private Role role;
}
