package com.example.demo.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductRequest {
    @NotBlank(message = "nama tidak boleh kosong")
    private String name;

    @NotNull(message = "harga tidak boleh kosong") // Gunakan NotNull untuk Double
    @Positive(message = "harga harus lebih dari 0") // Tambahan opsional agar harga valid
    private Double price; 
}
