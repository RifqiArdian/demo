package com.example.demo.web.controller;

import com.example.demo.application.service.AuthService;
import com.example.demo.web.dto.AuthResponse;
import com.example.demo.web.dto.LoginRequest;
import com.example.demo.web.dto.WebResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<WebResponse<String>> register(@Valid @RequestBody LoginRequest request) {
        String username = authService.register(request);
        return ResponseEntity.ok(WebResponse.<String>builder()
            .message("Register berhasil")
            .data(username)
            .build());
    }

    @PostMapping("/login")
    public ResponseEntity<WebResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        String token = authService.login(request);
        return ResponseEntity.ok(WebResponse.<AuthResponse>builder()
            .message("Login berhasil")
            .data(new AuthResponse(token))
            .build());
    }
}