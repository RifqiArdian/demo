package com.example.demo.infrastructure.repository;

import com.example.demo.domain.model.Product;
import com.example.demo.domain.repository.ProductRepository;
import com.example.demo.infrastructure.entity.ProductEntity;
import com.example.demo.infrastructure.mapper.ProductMapper; // Pastikan import ini ada
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PostgresProductRepository implements ProductRepository {

    private final JpaProductRepository jpaRepository;

    @Override
    public Product save(Product product) {
        // Menggunakan Mapper: Domain -> Entity
        ProductEntity entity = ProductMapper.toEntity(product);
        ProductEntity saved = jpaRepository.save(entity);
        // Menggunakan Mapper: Entity -> Domain
        return ProductMapper.toDomain(saved);
    }

    @Override
    public List<Product> findAll() {
        return jpaRepository.findAll().stream()
                .map(ProductMapper::toDomain) // Mengonversi setiap list entity ke domain
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Product> findById(Long id) {
        // Cari di database, jika ada konversi ke Domain menggunakan Mapper
        return jpaRepository.findById(id)
                .map(ProductMapper::toDomain);
    }

    @Override
    public void deleteById(Long id) {
        // Langsung hapus menggunakan JPA
        jpaRepository.deleteById(id);
    }
}