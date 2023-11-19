package com.example.rsaapi.service;

import com.example.rsaapi.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String secret;

    public String generateToken(User user) {
        int expiration = 86400000;
        Map<String, String> claims = new HashMap<>();
        claims.put("login", user.getLogin());
        claims.put("id", String.valueOf(user.getId()));
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(new Date(new Date().getTime() + expiration))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }
}
