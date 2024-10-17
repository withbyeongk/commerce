package io.hhplus.commerce.presentation.dto;

import io.hhplus.commerce.domain.entity.Product;

import java.time.LocalDateTime;


public record ProductResponseDto(
        Long productId,
        String name,
        int price,
        int stock,
        String description,
        LocalDateTime createdAt
){

    public ProductResponseDto(Long productId, String name, int price, int stock, String description, LocalDateTime createdAt) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.description = description;
        this.createdAt = createdAt;
    }

    public ProductResponseDto(Product product) {
        this(product.getId(), product.getName(), product.getPrice(), product.getStock(), product.getDescription(), product.getCreatedAt());
    }
}