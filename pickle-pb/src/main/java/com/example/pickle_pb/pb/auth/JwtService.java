package com.example.pickle_pb.pb.auth;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class JwtService {

    public static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";


    public void validateToken(final String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token);
        } catch (ExpiredJwtException e) {
            // 토큰 만료 시 예외 처리
            throw new RuntimeException("Token has expired", e);
        } catch (UnsupportedJwtException e) {
            // 지원하지 않는 토큰 형식 시 예외 처리
            throw new RuntimeException("Unsupported JWT token", e);
        } catch (MalformedJwtException e) {
            // 잘못된 토큰 형식 시 예외 처리
            throw new RuntimeException("Malformed JWT token", e);
        } catch (IllegalArgumentException e) {
            // 토큰이 null 또는 빈 문자열일 경우 예외 처리
            throw new RuntimeException("JWT token is not valid", e);
        }
    }


    public String generateToken(String pbnumber) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, pbnumber);
    }

    private String createToken(Map<String, Object> claims, String pbnumber) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(pbnumber)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30))
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject(); // userid는 토큰의 subject에 저장됨
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
