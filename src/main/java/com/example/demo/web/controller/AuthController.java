package com.example.demo.web.controller;

import com.example.demo.application.exception.AuthenticationFailedException;
import com.example.demo.application.exception.UsernameAlreadyUsedException;
import com.example.demo.application.service.AuthService;
import com.example.demo.web.dto.AuthResponse;
import com.example.demo.web.dto.LoginRequest;
import com.example.demo.web.dto.WebResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<WebResponse<String>> register(@Valid @RequestBody LoginRequest request) {
        try {
            String username = authService.register(request);
            return ResponseEntity.ok(WebResponse.<String>builder()
                .message("Register berhasil")
                .data(username).build());
        } catch (UsernameAlreadyUsedException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(WebResponse.<String>builder()
                .message(e.getMessage()).build());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<WebResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        try {
            String token = authService.login(request);
            return ResponseEntity.ok(WebResponse.<AuthResponse>builder()
                .message("Login berhasil")
                .data(new AuthResponse(token)).build());
        } catch (AuthenticationFailedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(WebResponse.<AuthResponse>builder()
                .message(e.getMessage()).build());
        }
    }
}