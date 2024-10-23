package io.hhplus.commerce.presentation.controller.product.dto;

public record ProductRequestDto(
        String name,
        int price,
        int stock,
        String description
) {
}
