package com.example.inventorycontrol.security;

import com.example.inventorycontrol.entity.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@SuppressWarnings("squid:S1068")
public class JwtTokens {
    private String accessToken;
    private String refreshToken;
    private Role role; // костыль! По факту нужно дешифровать access на фронте и тащить роль оттуда, просто я не умею, дай Бог переделаю
}
