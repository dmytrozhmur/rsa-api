package com.example.rsaapi.service;

import com.example.rsaapi.dto.KeyDTO;
import com.example.rsaapi.utils.Encryption;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

@Slf4j
@Service
public class KeyService {
    public KeyDTO getRsaKey() {
        RSAPublicKey key = (RSAPublicKey) Encryption.getKeyPair().getPublic();
        List<Byte> keyBytes = new ArrayList<>();
        for (byte b : key.getEncoded()) {
            keyBytes.add(b);
        }
        String keyString = String.format(
                "-----BEGIN PUBLIC KEY-----%s-----END PUBLIC KEY-----",
                Base64.getEncoder().encodeToString(key.getEncoded()));

        log.info("Generated key: {}", keyString);
        return new KeyDTO(keyString, keyBytes);
    }
}