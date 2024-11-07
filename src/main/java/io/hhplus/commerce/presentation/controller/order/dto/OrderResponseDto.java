package io.hhplus.commerce.presentation.controller.order.dto;

import io.hhplus.commerce.presentation.dataflatform.dto.OrderInfoToDataPlatformDto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.util.List;

public record OrderResponseDto(
    @Schema(description = "주문 ID")
    Long orderId,
    @Schema(description = "회원 ID")
    Long memberId,
    @Schema(description = "총 상품 금액")
    int totalPrice
){}
