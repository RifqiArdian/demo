package com.example.demo.application.service;

import com.example.demo.domain.model.User;
import com.example.demo.domain.repository.UserRepository;
import com.example.demo.infrastructure.security.JwtService;
import com.example.demo.web.dto.LoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public String register(LoginRequest request) throws Exception {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new Exception("Username sudah digunakan");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        // Encoding dilakukan di sini
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        
        userRepository.save(user);
        return user.getUsername();
    }

    public String login(LoginRequest request) throws Exception {
        return userRepository.findByUsername(request.getUsername())
            .filter(user -> passwordEncoder.matches(request.getPassword(), user.getPassword()))
            .map(user -> jwtService.generateToken(user.getUsername()))
            .orElseThrow(() -> new Exception("Username atau password salah!"));
    }
}