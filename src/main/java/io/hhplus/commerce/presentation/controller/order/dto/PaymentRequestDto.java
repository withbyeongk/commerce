package io.hhplus.commerce.presentation.controller.order.dto;

public record PaymentRequestDto(
        Long orderId,
        Long memberId,
        int amount
) {
}
