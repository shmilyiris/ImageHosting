package org.project.ImageHosting.admin.toolkit;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

// JWT工具类
@Component
public class JwtUtil {

    private static String secretKey = "ImageHosting_secret_key"; // 密钥
    private final static long expirationTime = 1000 * 60 * 60 * 2; // 过期时间 2h

    // 从token中获取用户名
    private static String getUsernameFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // 生成Token
    public static String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    // 验证Token
    public static boolean validateToken(String token, String username) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey) // 设置签名密钥
                    .parseClaimsJws(token) // 解析Claims
                    .getBody(); // 获取Claims主体

            return username.equals(claims.getSubject()); // 验证用户名是否匹配
        } catch (Exception e) {
            return false; // 捕获异常，返回无效
        }
    }

    // 刷新Token
    public static String refreshToken(String token) {
        // 解析原始Token
        String username = getUsernameFromToken(token);
        if (username != null && validateToken(token, username)) {
            return generateToken(username); // 重新创建Token
        }
        return null; // 如果Token无效或过期，返回null
    }
}

