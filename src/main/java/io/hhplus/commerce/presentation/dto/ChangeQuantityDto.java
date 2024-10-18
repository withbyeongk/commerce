package io.hhplus.commerce.presentation.dto;

public record ChangeQuantityDto(
    Long cartId,
    int quantity
) {
}
