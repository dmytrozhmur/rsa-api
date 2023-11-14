package com.example.rsaapi.service;

import com.example.rsaapi.dto.KeyDTO;
import com.example.rsaapi.utils.Encryption;
import org.springframework.stereotype.Service;

import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

@Service
public class KeyService {
    public KeyDTO getRsaKey() {
        RSAPublicKey key = (RSAPublicKey) Encryption.getKeyPair().getPublic();
        List<Byte> bytes = new ArrayList<>();
        for (byte b : key.getEncoded()) {
            bytes.add(b);
        }
        String string = Base64.getEncoder().encodeToString(key.getEncoded());
        String modulus = key.getModulus().toString();
        String exponent = key.getPublicExponent().toString();

        return new KeyDTO(string, bytes, modulus, exponent);
    }
}
