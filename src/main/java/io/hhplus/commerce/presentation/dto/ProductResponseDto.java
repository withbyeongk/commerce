package io.hhplus.commerce.presentation.dto;

import io.hhplus.commerce.domain.entity.Product;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;


public record ProductResponseDto(
        @Schema(description = "상품 ID")
        Long productId,
        @Schema(description = "상품명")
        String name,
        @Schema(description = "가격")
        int price,
        @Schema(description = "재고")
        int stock,
        @Schema(description = "상품 설명")
        String description,
        @Schema(description = "상품 등록일시")
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