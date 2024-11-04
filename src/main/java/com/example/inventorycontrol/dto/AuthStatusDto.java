package com.example.inventorycontrol.dto;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
@SuppressWarnings("squid:S1068")
public class AuthStatusDto {
    private String message;
    private UserDto data;
}
