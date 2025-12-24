package com.example.demo.web.controller;

import com.example.demo.domain.model.Product;
import com.example.demo.domain.repository.ProductRepository;
import com.example.demo.web.dto.WebResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductRepository productRepository;

    @GetMapping
    public ResponseEntity<WebResponse<List<Product>>> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return ResponseEntity.ok(
            WebResponse.<List<Product>>builder()
                .message("Berhasil mengambil data produk")
                .data(products)
                .build()
        );
    }

    @PostMapping
    public ResponseEntity<WebResponse<Product>> createProduct(@RequestBody Product product) {
        Product saved = productRepository.save(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(
            WebResponse.<Product>builder()
                .message("Produk berhasil ditambahkan")
                .data(saved)
                .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Long id) {
        return productRepository.findById(id)
            .map(product -> ResponseEntity.ok(
                WebResponse.<Product>builder()
                    .message("Berhasil mengambil data produk")
                    .data(product)
                    .build()
            ))
            .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                WebResponse.<Product>builder()
                    .message("Produk dengan ID " + id + " tidak ditemukan")
                    .build()
            ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return ResponseEntity.ok(
                WebResponse.builder()
                    .message("Produk berhasil dihapus")
                    .build()
            );
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                WebResponse.builder()
                    .message("Gagal menghapus! Produk dengan ID " + id + " tidak ditemukan")
                    .build()
            );
        }
    }
}