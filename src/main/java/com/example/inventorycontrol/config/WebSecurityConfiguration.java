package com.example.inventorycontrol.config;


import com.example.inventorycontrol.config.properties.UrlProperties;
import com.example.inventorycontrol.security.JwtTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfiguration {

    private final UrlProperties urlProperties;
    private final JwtTokenFilter jwtTokenFilter;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        String[] openUrls = getOpenUrls();
        return http
                .authorizeHttpRequests(request ->
                        request
//                                .requestMatchers(HttpMethod.POST,"/api/registration").permitAll()
//                                .requestMatchers(HttpMethod.POST,"/api/login").permitAll()
//                                .requestMatchers(HttpMethod.GET,"/api/login").permitAll()
//                                .requestMatchers(HttpMethod.POST,"/api/v1/orders/add/{id}/{quality}").authenticated()
//                                .requestMatchers(HttpMethod.POST,"/api/v1/products").authenticated()
                                .requestMatchers(HttpMethod.GET,"/api/v1/user").authenticated()
                                .requestMatchers(HttpMethod.GET,"/api/v1/orders").authenticated()
                                .anyRequest().permitAll()
                )
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .build();


    }
    private String[] getOpenUrls() {
        return urlProperties.getOpen().values().stream()
                .map(v -> v.split(", "))
                .flatMap(Arrays::stream)
                .toArray(String[]::new);
    }

}
