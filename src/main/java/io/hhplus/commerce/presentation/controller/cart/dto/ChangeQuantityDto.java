package io.hhplus.commerce.presentation.controller.cart.dto;

public record ChangeQuantityDto(
    Long cartId,
    int quantity
) {
}
