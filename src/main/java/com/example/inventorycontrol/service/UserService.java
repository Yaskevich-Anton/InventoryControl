package com.example.inventorycontrol.service;

import com.example.inventorycontrol.dto.UserDto;
import com.example.inventorycontrol.entity.User;
import com.example.inventorycontrol.mapper.UserMapper;
import com.example.inventorycontrol.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.NotFound;
import org.springframework.core.SpringVersion;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final SecurityContextResolverService securityContextResolverService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public ResponseEntity<?> updateUser(String login, UserDto userDto) {
        log.info("Updating user with login: " + login);
        Optional<User> userOptional = userRepository.findByUsername(login);
        if(userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        User userFromDb = userOptional.get();
        log.debug("User with name {}: retrieved from the database ", userFromDb.getUsername());

        User userAuth = getAuthUser();
        String role = userAuth.getRole().toString();
        if(!role.equals("ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Role is not ADMIN");
        }
        log.debug("Email validation");
        String email = userDto.getEmail();
        if (isValid(email)){
            userFromDb.setEmail(email);
        }
        else return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Email is not valid");

        userFromDb.setCountry(userDto.getCountry());
        if(userDto.getPassword() != null) {
            userFromDb.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }

        if(userRepository.findByUsername(userDto.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
        }
        userFromDb.setUsername(userDto.getUsername());

        userFromDb.setRole(userDto.getRole());
        userFromDb.setEmail(email);

        return ResponseEntity.status(HttpStatus.OK).body(userRepository.save(userFromDb));
//      user.setRole(userDto.getRole());
//      user.setEmail(userDto.getEmail());
//      user.setCountry(userDto.getCountry());
//      user.setPassword(userDto.getPassword());
//      user.setUsername(userDto.getUsername());
//      return userRepository.save(user);
    }
    private User getAuthUser(){
        UserDto currentUserDto = securityContextResolverService.getUserFromAuth();
        log.debug("Current user: {}", currentUserDto);
        User user = userMapper.toUser(currentUserDto);
        log.debug("User: {}", user);
        return user;
    }
    public static boolean isValid(String email) {
        return email.matches("^[\\w-\\.]+@[\\w-]+(\\.[\\w-]+)*\\.[a-z]{2,}$");
    }
}
