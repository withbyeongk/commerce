package io.hhplus.commerce.presentation.dto;

import io.hhplus.commerce.domain.entity.Product;

import java.time.LocalDateTime;


public record ProductResponseDto(
        Long productId,
        String name,
        int price,
        int stock,
        String description,
        LocalDateTime deletedAt,
        LocalDateTime updatedAt,
        LocalDateTime createdAt
){
    public ProductResponseDto(Product product) {
        this(product.getId(), product.getName(), product.getPrice(), product.getStock(), product.getDescription(), product.getDeletedAt(), product.getUpdatedAt(), product.getCreatedAt());
    }
}