package io.hhplus.commerce.presentation.dto;

public record OrderResponseDto(
    Long orderId,
    Long memberId,
    Long productId,
    int totalPrice
) {
}
