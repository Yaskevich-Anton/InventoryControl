package com.example.inventorycontrol.controller;

import com.example.inventorycontrol.dto.UserDto;
import com.example.inventorycontrol.entity.User;
import com.example.inventorycontrol.service.SecurityContextResolverService;
import com.example.inventorycontrol.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
public class UserController {
    private final UserService userService;
    private final SecurityContextResolverService securityContextResolverService;
    @GetMapping("user")
    public ResponseEntity<UserDto> getUser() {
        return ResponseEntity.ok().body(securityContextResolverService.getUserFromAuth());
    }
    @GetMapping("allUsers")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok().body(userService.getAllUsers());
    }
    @PostMapping("user/{username}")
    public ResponseEntity<?> updateUser(@PathVariable String username, @RequestBody UserDto userDto) {
       return userService.updateUser(username, userDto);
    }
}
