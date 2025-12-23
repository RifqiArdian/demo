package com.example.demo.web.controller;

import com.example.demo.infrastructure.entity.UserEntity;
import com.example.demo.infrastructure.repository.UserRepository;
import com.example.demo.infrastructure.security.JwtService;
import com.example.demo.web.dto.AuthResponse;
import com.example.demo.web.dto.LoginRequest;
import com.example.demo.web.dto.WebResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody LoginRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body(
                WebResponse.builder().message("Username sudah digunakan").build()
            );
        }

        UserEntity user = new UserEntity();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);

        return ResponseEntity.ok(
            WebResponse.builder()
                .message("Register berhasil")
                .data(request.getUsername())
                .build()
        );
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        return userRepository.findByUsername(request.getUsername())
            .filter(user -> passwordEncoder.matches(request.getPassword(), user.getPassword()))
            .map(user -> {
                String token = jwtService.generateToken(user.getUsername());
                return ResponseEntity.ok(
                    WebResponse.builder()
                        .message("Login berhasil")
                        .data(new AuthResponse(token))
                        .build()
                );
            })
            .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                WebResponse.builder()
                    .message("Username atau password salah!")
                    .build()
            ));
    }
}