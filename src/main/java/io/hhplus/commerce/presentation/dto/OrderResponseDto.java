package io.hhplus.commerce.presentation.dto;

import java.io.Serializable;
import java.util.List;

public record OrderResponseDto(
    Long orderId,
    Long memberId,
    int totalPrice,
    List<OrderItemRequestDto> products
) implements Serializable {
    public OrderInfoToDataPlatformDto toOrderInfoDto() {
        return new OrderInfoToDataPlatformDto(
                orderId,
                totalPrice,
                products
        );
    }

    public record OrderItemRequestDto(
            Long productId,
            int amount
    ) {

    }
}
