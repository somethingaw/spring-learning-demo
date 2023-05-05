package com.sfaw.springsecurityknife.service;

import com.sfaw.springsecurityknife.constants.CommonConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
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

    public String getTokenFromReq(HttpServletRequest request) {
        String authorization = request.getHeader(CommonConstants.AUTHORIZATION);
        if (StringUtils.isBlank(authorization)) {
            return null;
        }
        return getRealToken(authorization);
    }

    public String getRealToken(String token) {
        if (token.startsWith(CommonConstants.BEARER)) {
            return token.substring(CommonConstants.HEADER_PREFIX_LENGTH);
        } else {
            return token;
        }
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


    public String refreshToken(String oldToken) {
        String token = getRealToken(oldToken);
        Claims claims = getClaims(token);
        // 如果token在30分钟之内刚刷新过，返回原token
        // 这里也可以进行失效时间的延长
        if (tokenRefreshJustBefore(claims)) {
            return oldToken;
        } else {
            return generateToken(claims.getSubject());
        }
    }

    /**
     * 判断token在指定时间内是否刚刚刷新过
     */
    private boolean tokenRefreshJustBefore(Claims claims) {
        Date refreshDate = new Date();
        if (refreshDate.after(claims.getExpiration()) && refreshDate.before(DateUtils.addMinutes(claims.getExpiration(), 30))) {
            return true;
        }
        return false;
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
