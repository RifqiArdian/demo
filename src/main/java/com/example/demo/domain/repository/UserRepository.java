package com.example.demo.domain.repository;

import com.example.demo.domain.model.User;
import com.example.demo.infrastructure.entity.UserEntity;
import java.util.Optional;

public interface UserRepository {
    Optional<UserEntity> findByUsername(String username);
    void save(User user);
}