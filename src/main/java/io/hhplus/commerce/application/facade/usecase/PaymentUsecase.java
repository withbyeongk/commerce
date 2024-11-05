package io.hhplus.commerce.application.facade.usecase;

import io.hhplus.commerce.presentation.controller.order.dto.PaymentRequestDto;
import io.hhplus.commerce.presentation.controller.order.dto.PaymentResponseDto;

public interface PaymentUsecase {
    public PaymentResponseDto payment(PaymentRequestDto dto);
}
