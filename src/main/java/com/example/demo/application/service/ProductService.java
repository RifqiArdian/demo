package com.example.demo.application.service;

import com.example.demo.domain.model.Product;
import com.example.demo.domain.repository.ProductRepository;

import com.example.demo.infrastructure.mapper.ProductMapper;
import com.example.demo.web.dto.ProductRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public Product createProduct(ProductRequest productRequest) {
        Product product = ProductMapper.toModel(productRequest);
        return productRepository.save(product);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getById(Long id) {
        // Logika Optional dikelola di sini
        return productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produk tidak ditemukan"));
    }

    public void delete(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Gagal menghapus! Produk tidak ditemukan");
        }
        productRepository.deleteById(id);
    }

    public Product update(Long id, ProductRequest request) {
        // 1. Cari produknya, jika tidak ada lempar error 404
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produk tidak ditemukan"));

        // 2. Update datanya
        product.setName(request.getName());
        product.setPrice(request.getPrice());

        // 3. Simpan kembali
        productRepository.save(product);

        return product;
    }
}