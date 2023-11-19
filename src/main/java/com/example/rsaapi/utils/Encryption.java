package com.example.rsaapi.utils;

import lombok.Getter;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class Encryption {
    private static KeyPairGenerator generator;
    private static KeyFactory keyFactory;
    private static Cipher cipher;
    @Getter
    private static KeyPair keyPair;

    public static final String RSA = "RSA";

    static {
        try {
            keyFactory = KeyFactory.getInstance(RSA);
            cipher = Cipher.getInstance(RSA);
            generator = KeyPairGenerator.getInstance(RSA);
            generator.initialize(2048);
            keyPair = generator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e.getMessage());
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] encrypt(byte[] message, PublicKey publicKey) {
        try {
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            cipher.update(message);
            return cipher.doFinal();
        } catch (IllegalBlockSizeException | BadPaddingException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

    public static String decrypt(byte[] message, PrivateKey privateKey) {
        try {
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            cipher.update(message);
            return new String(cipher.doFinal(), StandardCharsets.UTF_8);
        } catch (IllegalBlockSizeException | BadPaddingException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

    public static PublicKey getPublicKey(byte[] keyBytes) {
        try {
            return keyFactory.generatePublic(new X509EncodedKeySpec(keyBytes));
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    public static PrivateKey getPrivateKey(BigInteger modulus, BigInteger exponent) {
        try {
            return keyFactory.generatePrivate(new RSAPrivateKeySpec(modulus, exponent));
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }
}
