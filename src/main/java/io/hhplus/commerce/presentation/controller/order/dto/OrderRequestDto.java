package io.hhplus.commerce.presentation.controller.order.dto;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public record OrderRequestDto(
        Long memberId,
        List<OrderItemRequestDto> products
) implements Serializable {
    public record OrderItemRequestDto(
            Long productId,
            int quantity
    ) {

    }
    public String getLockKey() {
        return this.products().stream()
                .map(OrderItemRequestDto::productId)
                .sorted()
                .map(Object::toString)
                .collect(Collectors.joining(","));
    }
}
