package io.hhplus.commerce.application.facade.usecase;

import io.hhplus.commerce.presentation.controller.order.dto.OrderRequestDto;
import io.hhplus.commerce.presentation.controller.order.dto.OrderResponseDto;

public interface OrderUsecase {
    OrderResponseDto makeOrder(OrderRequestDto dto);
}
