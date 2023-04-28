package com.sfaw.springsecurityknife.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * JwtService
 *
 * @author ArthurW
 * @version 1.0
 * @date 2023/4/21 10:12
 **/
@Service
@Slf4j
public class JwtService {


    @Value("${authjwt.secret.key}")
    private String secretKey;

    @Value("${authjwt.expiration}")
    private Long expiration;

    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * todo try catch掉
     * @param token
     * @return
     */
    public Claims getClaims(String token) {
        Claims claims = null;
        try {
            claims = Jwts.parserBuilder()
                    .setSigningKey(getKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            log.error("Token解析失败，token已经过期或者不正确，：{}", token, e);
        }
        return claims;
    }



    public static void main(String[] args) {
        String key = "wofqufwiefnqweofeqwefdqowehdfqiondklasncvaskdfvjbqklfweefqwefascdasdgfSF";
        String token = Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject("admin")
                .setExpiration(new Date(System.currentTimeMillis() + 100000))
                .signWith(Keys.hmacShaKeyFor(key.getBytes()), SignatureAlgorithm.HS512)
                .claim("username", "admin")
                .claim("id", "123")
                .compact();
        System.out.println(token);
        Claims claims = Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(key.getBytes())).build().parseClaimsJws(token).getBody();
    }
}
