package io.hhplus.commerce.presentation.dto;

public record ChangeQuantityDto(
    Long memberId,
    Long productId,
    int quantity
) {
}
