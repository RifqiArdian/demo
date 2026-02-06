package com.example.demo.web.controller;

import com.example.demo.application.service.ProductService;
import com.example.demo.domain.model.Product;
import com.example.demo.web.dto.ProductRequest;
import com.example.demo.web.dto.WebResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<WebResponse<List<Product>>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(
            WebResponse.<List<Product>>builder()
                .message("Berhasil mengambil data produk")
                .data(products)
                .build()
        );
    }

    @PostMapping
    public ResponseEntity<WebResponse<Product>> createProduct(@Valid @RequestBody ProductRequest productRequest) {
        Product saved = productService.createProduct(productRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(
            WebResponse.<Product>builder()
                .message("Produk berhasil ditambahkan")
                .data(saved)
                .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<WebResponse<Product>> getById(@PathVariable Long id) {
        // Cukup panggil satu baris ini
        Product product = productService.getById(id);

        return ResponseEntity.ok(WebResponse.<Product>builder()
                .message("Berhasil mengambil data")
                .data(product)
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<WebResponse<String>> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.ok(WebResponse.<String>builder()
                .message("Produk berhasil dihapus")
                .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<WebResponse<Product>> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequest request) {

        Product updatedProduct = productService.update(id, request);

        return ResponseEntity.ok(WebResponse.<Product>builder()
                .message("Produk berhasil diperbarui")
                .data(updatedProduct)
                .build());
    }
}