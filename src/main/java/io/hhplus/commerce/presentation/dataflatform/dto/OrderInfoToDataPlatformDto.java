package io.hhplus.commerce.presentation.dataflatform.dto;

import io.hhplus.commerce.presentation.controller.order.dto.OrderResponseDto;

import java.io.Serializable;
import java.util.List;

public record OrderInfoToDataPlatformDto(
        Long orderId,
        int totalPrice,
        List<OrderResponseDto.OrderItemRequestDto> products
) implements Serializable {
    public record OrderItemRequestDto(
            Long productId,
            int amount
    ) {

    }
}