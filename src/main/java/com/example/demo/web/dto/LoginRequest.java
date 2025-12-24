package com.example.demo.web.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank(message = "username tidak boleh kosong")
    private String username;

    @NotBlank(message = "password tidak boleh kosong")
    private String password;
}