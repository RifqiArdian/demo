package com.example.demo.infrastructure.repository;

import com.example.demo.infrastructure.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    // Method ini wajib ada untuk mencari user saat login
    Optional<UserEntity> findByUsername(String username);
}