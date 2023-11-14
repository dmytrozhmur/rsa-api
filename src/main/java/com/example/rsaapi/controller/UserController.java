package com.example.rsaapi.controller;

import com.example.rsaapi.dto.UserDTO;
import com.example.rsaapi.entity.User;
import com.example.rsaapi.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public Optional<User> getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PostMapping("/signup")
    public UserDTO registerUser(@RequestBody UserDTO user) {
        return userService.createUser(user);
    }

    @PostMapping("/signin")
    public UserDTO authenticateUser(@RequestBody UserDTO user) {
        return userService.verifyUser(user);
    }

    @PostMapping("/create-multiple")
    public void createMultipleUsers(@RequestBody User[] users) {
        userService.createMultipleUsers(users);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}
