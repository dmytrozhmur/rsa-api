package com.example.rsaapi.repository;

import com.example.rsaapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByLogin(String login);
    boolean existsByLogin(String login);
}
