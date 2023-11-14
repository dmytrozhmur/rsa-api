package com.example.rsaapi.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String login;
    private String password;

    public User() {
    }

    public User(Long id) {
        this.id = id;
    }
}
