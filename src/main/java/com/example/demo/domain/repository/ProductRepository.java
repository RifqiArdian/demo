package com.example.demo.domain.repository;

import com.example.demo.domain.model.Product;
import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    Product save(Product product);
    List<Product> findAll();
    Optional<Product> findById(Long id);
    void deleteById(Long id);
    boolean existsById(Long id);
}