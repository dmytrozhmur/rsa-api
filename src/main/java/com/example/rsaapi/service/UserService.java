package com.example.rsaapi.service;

import com.example.rsaapi.dto.UserDTO;
import com.example.rsaapi.entity.User;
import com.example.rsaapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import static com.example.rsaapi.utils.Encryption.*;

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

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        KeyPair keys = getKeyPair();
        Optional<User> tempUser = userRepository.findById(id);

        String login = tempUser.get().getLogin();
        String password = tempUser.get().getPassword();
        String decryptedLogin = decrypt(Base64.getDecoder().decode(login), keys.getPrivate());
        String decryptedPassword = decrypt(Base64.getDecoder().decode(password), keys.getPrivate());

        tempUser.get().setLogin(decryptedLogin);
        tempUser.get().setPassword(decryptedPassword);

        return tempUser;
    }

    public UserDTO createUser(UserDTO user) {
        KeyPair keys = getKeyPair();
        User entity = new User();
        UserDTO response = new UserDTO();

        String decryptedLogin = decrypt(Base64.getDecoder().decode(user.getLogin()), keys.getPrivate());
        String decryptedPassword = decrypt(Base64.getDecoder().decode(user.getPassword()), keys.getPrivate());
        entity.setLogin(decryptedLogin);
        entity.setPassword(passwordEncoder.encode(decryptedPassword));
        entity = userRepository.save(entity);

        String encryptedId = getEncrypted(String.valueOf(entity.getId()), user.getPublicKey());
        String encryptedLogin = getEncrypted(entity.getLogin(), user.getPublicKey());
        response.setId(encryptedId);
        response.setLogin(encryptedLogin);

        return response;
    }

    public void createMultipleUsers(User[] users) {
        for (User user : users) {
            userRepository.save(user);
        }
    }

    public boolean deleteUser(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if(userOptional.isPresent()) {
            userRepository.delete(userOptional.get());
            return true;
        }
        return false;
    }

    public UserDTO verifyUser(UserDTO user) {
        KeyPair keys = getKeyPair();
        User entity = new User();
        UserDTO response = new UserDTO();

        String decryptedLogin = decrypt(Base64.getDecoder().decode(user.getLogin()), keys.getPrivate());
        String decryptedPassword = decrypt(Base64.getDecoder().decode(user.getPassword()), keys.getPrivate());

        entity.setLogin(decryptedLogin);
        entity.setPassword(decryptedPassword);
        if (userExistsInDb(entity)) {
            response.setAccessToken(getEncrypted(jwtService.generateToken(user), user.getPublicKey()));
        } else {
            response.setErrors(new String[] { getEncrypted("Invalid login or password", user.getPublicKey()) });
        }

        return response;
    }

    private boolean userExistsInDb(User checkedUser) {
        List<User> existingUsers = userRepository.findAllByLogin(checkedUser.getLogin());
        return existingUsers != null && !existingUsers.isEmpty()
                && existingUsers.stream().anyMatch(user ->
                passwordEncoder.matches(checkedUser.getPassword(), user.getPassword()));
    }

    private static String getEncrypted(String login, byte[] receivedKey) {
        return Base64
                .getEncoder()
                .encodeToString(encrypt(login.getBytes(StandardCharsets.UTF_8), getPublicKey(receivedKey)));
    }
}
