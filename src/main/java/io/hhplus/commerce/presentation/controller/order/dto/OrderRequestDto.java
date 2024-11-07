package io.hhplus.commerce.presentation.controller.order.dto;

import java.io.Serializable;
import java.util.List;

public record OrderRequestDto(
        Long memberId,
        List<OrderItemRequestDto> products
) implements Serializable {
    public record OrderItemRequestDto(
            Long productId,
            int quantity
    ) {

    }
}
