package com.example.inventorycontrol.security;


import com.example.inventorycontrol.entity.User;
import com.example.inventorycontrol.exception.JwtAuthenticationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private static final String ROLES_KEY = "roles";
    private static final String ID_KEY = "userId";
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String TOKEN_START_OF_LINE = "Bearer ";
    private static final String IS_REFRESH = "isRefresh";

    @Value("${jwt.token.access.expireTimeInMinutes}")
    private Integer accessLifetime;
    @Value("${jwt.token.refresh.expireTimeInMinutes}")
    private Integer refreshLifetime;
    @Value("${jwt.token.secret}")
    private String secret;
    private SecretKey secretKey;

    private final RedissonClient redissonClient;

    @PostConstruct
    private void init() {
        secret = Base64.getEncoder().encodeToString(secret.getBytes());
        secretKey = Keys.hmacShaKeyFor(secret.getBytes());
    }


    public String createAccessToken(User user) {
        Claims claims = Jwts.claims()
                .subject(user.getUsername())
                .add(ROLES_KEY, user.getRole())
                .add(ID_KEY, user.getId())
                .add(IS_REFRESH, false)
                .build();

        return generateJwtToken(claims, accessLifetime);
    }

    private String generateJwtToken(Claims claims, int lifeTime) {
        var issuedAt = new Date();
        var expiration = new Date(issuedAt.getTime() + lifeTime * 60000L);

        return Jwts.builder()
                .claims(claims)
                .issuedAt(issuedAt)
                .expiration(expiration)
                .signWith(secretKey, Jwts.SIG.HS256)
                .compact();
    }

    public Authentication getAuthenticationFromToken(String token) {
        return new UsernamePasswordAuthenticationToken(getSubjectFromToken(token), "", getAuthorities(token));
    }


    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (bearerToken != null && bearerToken.startsWith(TOKEN_START_OF_LINE)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public String createRefreshToken(User user) {
        Claims claims = Jwts.claims()
                .subject(user.getUsername())
                .add(ID_KEY, user.getId())
                .add(IS_REFRESH, true)
                .build();

        return generateJwtToken(claims, refreshLifetime);
    }

    private Claims getClaimsFromToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            throw new JwtAuthenticationException("Token is expired");
        } catch (JwtException e) {
            throw new JwtAuthenticationException("Token is invalid");
        }
    }

    public UUID getUserIdFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        if (isTokenInBlacklist(token)){
            throw new JwtAuthenticationException("Token is expired");
        }
        return UUID.fromString(claims.get(ID_KEY).toString());
    }

    public String getSubjectFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        if (isTokenInBlacklist(token)){
            throw new JwtAuthenticationException("Token is expired");
        }
        return claims.getSubject();
    }

    private List<GrantedAuthority> getAuthorities(String token) {
        Claims claims = getClaimsFromToken(token);
        String authorities = claims.get(ROLES_KEY, String.class);

        if (authorities.isBlank()) {
            return Collections.emptyList();
        }
        return Arrays.stream(authorities.split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    public boolean validateRefreshToken(String token) {
        return getClaimsFromToken(token).get(IS_REFRESH, Boolean.class);
    }

    private boolean isTokenInBlacklist(String token){
        RMapCache<String, JwtTokens> rMapCache = redissonClient.getMapCache("blackList");
        return rMapCache.get(token) != null;
    }

}

