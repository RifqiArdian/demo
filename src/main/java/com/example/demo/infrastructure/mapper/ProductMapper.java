package com.example.demo.infrastructure.mapper;

import com.example.demo.domain.model.Product;
import com.example.demo.infrastructure.entity.ProductEntity;
import com.example.demo.web.dto.ProductRequest;

public class ProductMapper {

    public static Product toDomain(ProductEntity entity) {
        if (entity == null) {
            return null;
        }
        if (entity.getName() == null || entity.getName().isBlank()) {
            throw new IllegalArgumentException("Product name cannot be null or blank");
        }
        if (entity.getPrice() == null || entity.getPrice() <= 0) {
            throw new IllegalArgumentException("Product price must be greater than 0");
        }
        return new Product(
            entity.getId(),
            entity.getName(),
            entity.getPrice()
        );
    }

    public static ProductEntity toEntity(Product product) {
        if (product == null) {
            return null;
        }
        if (product.getName() == null || product.getName().isBlank()) {
            throw new IllegalArgumentException("Product name cannot be null or blank");
        }
        if (product.getPrice() == null || product.getPrice() <= 0) {
            throw new IllegalArgumentException("Product price must be greater than 0");
        }
        ProductEntity entity = new ProductEntity();
        if (product.getId() != null) {
            entity.setId(product.getId());
        }
        entity.setName(product.getName());
        entity.setPrice(product.getPrice());
        return entity;
    }

    public static Product toModel(ProductRequest productRequest) {
        if (productRequest == null) {
            return null;
        }
        Product model = new Product();
        model.setName(productRequest.getName());
        model.setPrice(productRequest.getPrice());
        return model;
    }
}