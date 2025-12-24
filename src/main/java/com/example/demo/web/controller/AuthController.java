package com.example.demo.web.controller;

import com.example.demo.application.service.AuthService;
import com.example.demo.web.dto.AuthResponse;
import com.example.demo.web.dto.LoginRequest;
import com.example.demo.web.dto.WebResponse;
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
    public ResponseEntity<WebResponse<String>> register(@RequestBody LoginRequest request) {
        try {
            String username = authService.register(request);
            return ResponseEntity.ok(WebResponse.<String>builder()
                .message("Register berhasil")
                .data(username).build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(WebResponse.<String>builder()
                .message(e.getMessage()).build());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<WebResponse<AuthResponse>> login(@RequestBody LoginRequest request) {
        try {
            String token = authService.login(request);
            return ResponseEntity.ok(WebResponse.<AuthResponse>builder()
                .message("Login berhasil")
                .data(new AuthResponse(token)).build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(WebResponse.<AuthResponse>builder()
                .message(e.getMessage()).build());
        }
    }
}