package com.example.demo.infrastructure.mapper;

import com.example.demo.domain.model.Product;
import com.example.demo.infrastructure.entity.ProductEntity;

public class ProductMapper {

    /**
     * Mengubah dari Database Entity ke Domain Model.
     * Digunakan saat mengambil data dari PostgreSQL untuk dibawa ke Logic Bisnis.
     */
    public static Product toDomain(ProductEntity entity) {
        if (entity == null) {
            return null;
        }
        return new Product(
            entity.getId(),
            entity.getName(),
            entity.getPrice()
        );
    }

    /**
     * Mengubah dari Domain Model ke Database Entity.
     * Digunakan saat ingin menyimpan data dari Logic Bisnis ke PostgreSQL.
     */
    public static ProductEntity toEntity(Product product) {
        if (product == null) {
            return null;
        }
        ProductEntity entity = new ProductEntity();
        entity.setId(product.getId());
        entity.setName(product.getName());
        entity.setPrice(product.getPrice());
        return entity;
    }
}