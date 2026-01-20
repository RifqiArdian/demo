package com.example.demo.infrastructure.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.example.demo.domain.model.User;
import com.example.demo.domain.repository.UserRepository;
import com.example.demo.infrastructure.entity.UserEntity;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PostgresUserRepository implements UserRepository {

    private final JpaUserRepository jpaUserRepository;

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        // Tetap mengembalikan Entity karena Service/Security membutuhkannya untuk mapping
        return jpaUserRepository.findByUsername(username);
    }

    @Override
    public void save(User user) {
        // PROSES MAPPING: Mengubah Domain Model (User) menjadi Database Entity (UserEntity)
        UserEntity entity = new UserEntity();
        entity.setUsername(user.getUsername());
        entity.setPassword(user.getPassword());
        
        // Simpan Entity ke database melalui JPA
        jpaUserRepository.save(entity);
    }
}