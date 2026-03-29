package com.smart.common.utils;

import io.jsonwebtoken.Claims;
import com.smart.common.constants.JwtConstants;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;


@Slf4j
@Component
public class JwtUtil {
    // 密钥
    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(JwtConstants.JWT_SECRET.getBytes());

    // 生成token
    public static String createToken(Map<String,Object> claims){
        return Jwts.builder()
                .claims(claims) // 自定义payload
                .issuedAt(new Date()) // 签发时间
                .expiration(new Date(System.currentTimeMillis() + JwtConstants.JWT_EXPIRE)) // 过期时间
                .signWith(SECRET_KEY) // 签名
                .compact();
    }

    // 解析token
    public static Claims parseToken(String token){
        return Jwts.parser()
                .verifyWith(SECRET_KEY) // 校验签名
                .build()
                .parseSignedClaims(token)// 解析token
                .getPayload(); // 获取payload
    }
}
