package com.example.inventorycontrol.security;

import com.example.inventorycontrol.dto.UserDto;
import com.example.inventorycontrol.dto.auth_requests.AuthRequestDto;
import com.example.inventorycontrol.dto.auth_requests.RegisterRequestDto;
import com.example.inventorycontrol.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface IAuthorizationService {
    ResponseEntity<Object> authenticate(String username, String password);

    ResponseEntity<Object> refresh(String refreshToken);

    ResponseEntity<Object> changePassword(String oldPassword, String newPassword);

    User register(UserDto request);

    void logout(JwtTokens userTokens);
}
