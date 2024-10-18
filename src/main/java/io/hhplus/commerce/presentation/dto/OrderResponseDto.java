package io.hhplus.commerce.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.util.List;

public record OrderResponseDto(
    @Schema(description = "주문 ID")
    Long orderId,
    @Schema(description = "회원 ID")
    Long memberId,
    @Schema(description = "총 상품 금액")
    int totalPrice,
    @Schema(description = "주문한 상품 목록")
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
            @Schema(description = "상품 ID")
            Long productId,
            @Schema(description = "주문한 상품 수")
            int amount
    ) {

    }
}
