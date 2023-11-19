package com.example.rsaapi.controller;

import com.example.rsaapi.dto.UserDTO;
import com.example.rsaapi.entity.User;
import com.example.rsaapi.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/api/users")
@ResponseStatus(HttpStatus.ACCEPTED)
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public UserDTO registerUser(@RequestBody UserDTO user) {
        return userService.createUser(user);
    }

    @PostMapping("/signin")
    public UserDTO authenticateUser(@RequestBody UserDTO user) {
        return userService.verifyUser(user);
    }
}
