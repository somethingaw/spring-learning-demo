package com.sfaw.springsecurityknife.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
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
        return Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(token).getBody();
    }



    public static void main(String[] args) {
        String key = "wofqufwiefnqweofeqwefdqowehdfqiondklasncvaskdfvjbqklfweefqwefascdasdgfSF";
        String token = Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject("wy")
                .setExpiration(new Date(System.currentTimeMillis() + 100000))
                .signWith(Keys.hmacShaKeyFor(key.getBytes()), SignatureAlgorithm.HS512)
                .claim("username", "wy")
                .claim("id", "123")
                .compact();
        System.out.println(token);
        Claims claims = Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(key.getBytes())).build().parseClaimsJws(token).getBody();
    }
}
