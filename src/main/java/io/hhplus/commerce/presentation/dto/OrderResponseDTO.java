package io.hhplus.commerce.presentation.dto;

public record OrderResponseDTO(
    Long orderId,
    Long memberId,
    Long productId,
    int totalPrice
) {
}
