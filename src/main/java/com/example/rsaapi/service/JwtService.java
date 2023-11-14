package com.example.rsaapi.service;

import com.example.rsaapi.dto.UserDTO;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;

@Service
public class JwtService {
    private SecretKey secret;

    public JwtService() {
        secret = generateSecretKey("secret");
    }

    public String generateToken(UserDTO user) {
        int expiration = 86400000;
        return Jwts.builder()
                .setSubject(user.getLogin())
                .setExpiration(new Date(new Date().getTime() + expiration))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public SecretKey generateSecretKey(String keyword) {
        byte[] decodedKey = Base64.getDecoder().decode(keyword);
        return new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
    }
}
