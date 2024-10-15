package io.hhplus.commerce.presentation.dto;

public record ProductRequestDto(
        String name,
        int price,
        int stock,
        String description
) {
}
