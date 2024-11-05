package com.example.inventorycontrol.security;

import com.example.inventorycontrol.dto.UserDto;
import com.example.inventorycontrol.entity.User;
import com.example.inventorycontrol.entity.enums.Role;
import com.example.inventorycontrol.exception.ApplicationException;
import com.example.inventorycontrol.exception.JwtAuthenticationException;
import com.example.inventorycontrol.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthorizationService implements IAuthorizationService {

    private final UserRepository userRepository;
    private final JwtTokenProvider tokenProvider;
    private final BCryptPasswordEncoder encoder;
    @Value("${jwt.token.refresh.expireTimeInMinutes}")
    private Integer expireTime;
    private final RedissonClient redissonClient;


    public ResponseEntity<Object> authenticate(String username, String password) {
        User user;
        try {
            user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new ApplicationException(HttpStatus.UNAUTHORIZED, "Не удалось найти пользователя"));
        } catch (ApplicationException exception) {
            return ResponseEntity.status(exception.getStatus()).body(exception.getMessage());
        }

        if (!encoder.matches(password, user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Неверный логин или пароль пользователя");

        }

        String accessToken = tokenProvider.createAccessToken(user);

        String refreshToken = tokenProvider.createRefreshToken(user);

        JwtTokens build = JwtTokens.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
        return ResponseEntity.ok(build);
    }

    @Override
    public ResponseEntity<Object> refresh(String refreshToken) {
        if (tokenProvider.validateRefreshToken(refreshToken)) {
            UUID userId = tokenProvider.getUserIdFromToken(refreshToken);
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new JwtAuthenticationException("Failed to find user with id: " + userId));

            return ResponseEntity.ok(JwtTokens.builder()
                    .accessToken(tokenProvider.createAccessToken(user))
                    .refreshToken(tokenProvider.createRefreshToken(user))
                    .build());
        } else {
            throw new JwtAuthenticationException("Token is invalid");
        }
    }
    @Override
    @Transactional
    public User register(UserDto request) {

        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new ApplicationException(
                    HttpStatus.CONFLICT,
                    format("User with name %s did not enter and confirm email before creating User", request.getUsername()));
        }
        boolean isPresent = userRepository.findByUsername(request.getUsername()).isPresent()
                         || userRepository.findByEmail(request.getEmail()).isPresent()
                         || userRepository.findByPhone(request.getPhoneNumber()).isPresent();
        if(isPresent) {
            throw new ApplicationException(
                    HttpStatus.CONFLICT,
                    format("User with name %s and email %s already exist. Please, change your name and email",request.getUsername(),request.getEmail())
            );
        }
        return registerLegatusUser(request);
    }

    public void logout(JwtTokens userTokens) {
        RMapCache<String, String> rMapCache = redissonClient.getMapCache("blackList");

        String accessToken = userTokens.getAccessToken();
        String refreshToken = userTokens.getRefreshToken();

        if (accessToken == null || refreshToken == null) {
            // Log the issue or handle it appropriately
            throw new IllegalArgumentException("Tokens cannot be null");
        }


        rMapCache.put(userTokens.getAccessToken(), userTokens.getAccessToken(), expireTime, TimeUnit.MINUTES);
        rMapCache.put(userTokens.getRefreshToken(), userTokens.getRefreshToken(), expireTime, TimeUnit.MINUTES);
    }

    private User registerLegatusUser(UserDto request) {
        return userRepository.save(User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .password(encoder.encode(request.getPassword()))
                .country(request.getCountry())
                .role(Role.valueOf(Role.USER.name()))
                .build());
    }



    public ResponseEntity<Object> changePassword(String oldPassword, String newPassword) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ApplicationException(HttpStatus.UNAUTHORIZED, "Пользователь не найден"));

        if (!encoder.matches(oldPassword, user.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Неверный пароль пользователя");
        }

        if (!isPasswordValid(newPassword)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Пароль не соответствует требованиям");
        }

        user.setPassword(encoder.encode(newPassword));
        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    private boolean isPasswordValid(String password) {
        String regex = "^[a-zA-Z0-9\"\\\\/'`~!@#$%^&*()_+=|\\- ]+$";
        return password != null && password.matches(regex);
    }

}