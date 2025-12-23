package com.example.demo.infrastructure.repository;

import com.example.demo.domain.repository.UserRepository;
import com.example.demo.infrastructure.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PostgresUserRepository implements UserRepository {

    private final JpaUserRepository jpaUserRepository;

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        return jpaUserRepository.findByUsername(username);
    }

    @Override
    public void save(UserEntity user) {
        jpaUserRepository.save(user);
    }
}