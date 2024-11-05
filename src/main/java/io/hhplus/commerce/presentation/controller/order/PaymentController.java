package io.hhplus.commerce.presentation.controller.order;

import io.hhplus.commerce.application.facade.usecase.PaymentUsecase;
import io.hhplus.commerce.presentation.controller.order.dto.PaymentRequestDto;
import io.hhplus.commerce.presentation.controller.order.dto.PaymentResponseDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/member/")
@RequiredArgsConstructor
@Tag(name = "/api/member/", description = "결제 API")
public class PaymentController {
    private final PaymentUsecase paymentUsecase;

    @PostMapping("/{memberId}/payment")
    public PaymentResponseDto payment(@RequestBody PaymentRequestDto dto) {
        return paymentUsecase.payment(dto);
    }
}
