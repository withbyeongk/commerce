package io.hhplus.commerce.presentation.controller.product.dto;

import io.hhplus.commerce.domain.product.Product;
import io.swagger.v3.oas.annotations.media.Schema;

import java.sql.Timestamp;


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
        Timestamp createdAt
){
    public ProductResponseDto(Product product) {
        this(product.getId(), product.getName(), product.getPrice(), product.getStock(), product.getDescription(), Timestamp.valueOf(product.getCreatedAt()));
    }
}