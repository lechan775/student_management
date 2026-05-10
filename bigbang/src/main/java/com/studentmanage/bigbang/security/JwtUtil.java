package com.studentmanage.bigbang.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * JWT 工具类 — 宇宙爆炸版
 * 支持 accessToken(短期) + refreshToken(长期)
 * accessToken: 1 小时，用于 API 鉴权
 * refreshToken: 7 天，用于无感刷新 accessToken
 */
@Component
public class JwtUtil {

    private final SecretKey key;
    private final long accessExpirationMs;
    private final long refreshExpirationMs;

    public JwtUtil(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access-token-expiration}") long accessExpirationMs,
            @Value("${jwt.refresh-token-expiration}") long refreshExpirationMs) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.accessExpirationMs = accessExpirationMs;
        this.refreshExpirationMs = refreshExpirationMs;
    }

    // ==================== 生成 ====================

    public String generateAccessToken(String username, String role) {
        return buildToken(username, role, accessExpirationMs);
    }

    public String generateRefreshToken(String username, String role) {
        return buildToken(username, role, refreshExpirationMs);
    }

    private String buildToken(String username, String role, long expiration) {
        return Jwts.builder()
                .subject(username)
                .claim("role", role)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key)
                .compact();
    }

    // ==================== 解析 ====================

    public String extractUsername(String token) {
        return parseClaims(token).getSubject();
    }

    public String extractRole(String token) {
        return parseClaims(token).get("role", String.class);
    }

    // ==================== 校验 ====================

    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
