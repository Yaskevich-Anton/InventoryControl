package com.example.inventorycontrol.service;

import com.example.inventorycontrol.dto.UserDto;
import com.example.inventorycontrol.entity.User;
import com.example.inventorycontrol.exception.UserAuthenticationException;
import com.example.inventorycontrol.mapper.UserMapper;
import com.example.inventorycontrol.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SecurityContextResolverService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserDto getUserFromAuth() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDto userId = getUserFromAuthForAccessToken(authentication);
        User user = userRepository.findById(userId.getId()).orElseThrow(() -> new UserAuthenticationException("User with id" + userId + " not found"));
        return userMapper.toUserDto(user);
    }

    private UserDto getUserFromAuthForAccessToken(Authentication authentication) {

        User user = userRepository.findByUsername((String) authentication.getPrincipal())
                .orElseThrow(() -> new UserAuthenticationException("User not found"));
        return userMapper.toUserDto(user);
    }
}
