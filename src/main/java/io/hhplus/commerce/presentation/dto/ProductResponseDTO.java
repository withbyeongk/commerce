package io.hhplus.commerce.presentation.dto;

import java.time.LocalDateTime;


public record ProductResponseDTO (
        Long productId,
        String name,
        int price,
        int stock,
        String description,
        LocalDateTime deletedAt,
        LocalDateTime updatedAt,
        LocalDateTime createdAt
){}