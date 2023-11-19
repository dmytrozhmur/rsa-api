package com.example.rsaapi.service;

import com.example.rsaapi.dto.UserDTO;
import com.example.rsaapi.entity.User;
import com.example.rsaapi.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.rsaapi.utils.Encryption.*;

@Slf4j
@Service
public class UserService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDTO createUser(UserDTO user) {
        KeyPair keys = getKeyPair();
        User entity = new User();
        UserDTO response = new UserDTO();
        byte[] clientKeyBytes = getDecodedKey(user.getPublicKey());
        String decryptedLogin = decrypt(Base64.getDecoder().decode(user.getLogin()), keys.getPrivate());
        String decryptedPassword = decrypt(Base64.getDecoder().decode(user.getPassword()), keys.getPrivate());

        if (userRepository.existsByLogin(decryptedLogin)) {
            String errorMessage = getEncrypted(
                    "Login is already in use".getBytes(StandardCharsets.UTF_8), clientKeyBytes);
            response.setErrors(Collections.singletonList(errorMessage));
            return response;
        }
        entity.setLogin(decryptedLogin);
        entity.setPassword(passwordEncoder.encode(decryptedPassword));
        entity = userRepository.save(entity);

        byte[] idBytes = String.valueOf(entity.getId()).getBytes(StandardCharsets.UTF_8);
        byte[] loginBytes = entity.getLogin().getBytes(StandardCharsets.UTF_8);
        String encryptedId = getEncrypted(idBytes, clientKeyBytes);
        String encryptedLogin = getEncrypted(loginBytes, clientKeyBytes);
        response.setId(encryptedId);
        response.setLogin(encryptedLogin);

        return response;
    }

    public UserDTO verifyUser(UserDTO user) {
        KeyPair keys = getKeyPair();
        UserDTO response = new UserDTO();
        byte[] clientKeyBytes = getDecodedKey(user.getPublicKey());

        String decryptedLogin = decrypt(Base64.getDecoder().decode(user.getLogin()), keys.getPrivate());
        String decryptedPassword = decrypt(Base64.getDecoder().decode(user.getPassword()), keys.getPrivate());
        User entity = userExistsInDb(decryptedLogin, decryptedPassword);
        if (entity != null) {
            String token = jwtService.generateToken(entity);
            byte[] tokenBytes = token.getBytes(StandardCharsets.UTF_8);
            log.info("Generated token: {}", token);
            response.setAccessToken(getEncrypted(tokenBytes, clientKeyBytes));
        } else {
            byte[] errorMessageBytes = "Invalid login or password".getBytes(StandardCharsets.UTF_8);
            response.setErrors(Collections.singletonList(getEncrypted(errorMessageBytes, clientKeyBytes)));
        }

        return response;
    }

    private User userExistsInDb(String login, String password) {
        User existingUser = userRepository.findByLogin(login);
        if (existingUser == null || passwordEncoder.matches(password, existingUser.getPassword())) {
            return existingUser;
        } else {
            return null;
        }
    }

    private static String getEncrypted(byte[] data, byte[] receivedKey) {
        return Base64
                .getEncoder()
                .encodeToString(encrypt(data, getPublicKey(receivedKey)));
    }

    private static byte[] getDecodedKey(String key) {
        String keyString = key.replaceAll("\r\n", "");
        log.info("Received key: {}", keyString);
        keyString = keyString.replaceAll("-----BEGIN PUBLIC KEY-----", "");
        keyString = keyString.replaceAll("-----END PUBLIC KEY-----", "");
        return Base64.getDecoder().decode(keyString);
    }
}
