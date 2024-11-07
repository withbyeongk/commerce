package io.hhplus.commerce.presentation.controller.member.dto;

public record ChangeQuantityDto(
    Long cartId,
    int quantity
) {
}
