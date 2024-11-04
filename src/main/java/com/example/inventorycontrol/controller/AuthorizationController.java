package com.example.inventorycontrol.controller;

import com.example.inventorycontrol.dto.AuthStatusDto;
import com.example.inventorycontrol.dto.UserDto;
import com.example.inventorycontrol.dto.auth_requests.AuthRequestDto;
import com.example.inventorycontrol.dto.auth_requests.ChangePasswordRequestDto;
import com.example.inventorycontrol.dto.auth_requests.RegisterRequestDto;
import com.example.inventorycontrol.entity.User;
import com.example.inventorycontrol.exception.JwtAuthenticationException;
import com.example.inventorycontrol.security.IAuthorizationService;
import com.example.inventorycontrol.security.JwtTokens;
import com.example.inventorycontrol.service.SecurityContextResolverService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Slf4j
@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthorizationController {
    private final IAuthorizationService authorizationService;

    @GetMapping("/login")
    public String getAutherizationPage(){
        return "authorization";
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@Validated @RequestBody AuthRequestDto request) {
        return authorizationService.authenticate(request.getUsername(), request.getPassword());
    }

    @PostMapping("/logout")
    @ResponseStatus (HttpStatus.OK)
    public void logout(@RequestBody JwtTokens userTokens) {
        authorizationService.logout(userTokens);
    }

    @PostMapping("/registration")
    @ResponseStatus(HttpStatus.CREATED)
    public User register(@Valid @RequestBody UserDto request) {

        return authorizationService.register(request);
    }

    @PostMapping("/refresh")
    public ResponseEntity<Object> refresh(@RequestBody JwtTokens jwtTokens) {

        if (jwtTokens.getRefreshToken() == null) {
            throw new JwtAuthenticationException("Запрос не содержит refresh-токена");
        }

        return authorizationService.refresh(jwtTokens.getRefreshToken());
    }


    @PostMapping("/login/change-password")
    public ResponseEntity<Object> changePassword(@Validated @RequestBody
                                                 ChangePasswordRequestDto requestDto) {
        return authorizationService.changePassword(requestDto.getOldPassword(), requestDto.getNewPassword());
    }
}
