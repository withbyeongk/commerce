package io.hhplus.commerce.presentation.controller.order.dto;

public record PaymentResponseDto(
        Long paymentId,
        Long memberId,
        Long orderId,
        int amount
) {
}
