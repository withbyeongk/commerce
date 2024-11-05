package io.hhplus.commerce.presentation.controller.member.dto;

import io.hhplus.commerce.domain.product.Product;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;


public record CartListDto(
        @Schema(description = "상품 ID")
        Long productId,
        @Schema(description = "상품명")
        String name,
        @Schema(description = "가격")
        int price,
        @Schema(description = "재고")
        int stock,
        @Schema(description = "담은 수량")
        int quantity,
        @Schema(description = "상품 설명")
        String description,
        @Schema(description = "상품 등록일시")
        LocalDateTime createdAt
){

    public CartListDto(Long productId, String name, int price, int stock, String description, LocalDateTime createdAt) {
        this(productId, name, price, stock, 0, description, createdAt);
    }

    public CartListDto(Product product) {
        this(product.getId(), product.getName(), product.getPrice(), product.getStock(), 0, product.getDescription(), product.getCreatedAt());
    }

    public CartListDto(Product product, int quantity) {
        this(product.getId(), product.getName(), product.getPrice(), product.getStock(), quantity, product.getDescription(), product.getCreatedAt());
    }

}