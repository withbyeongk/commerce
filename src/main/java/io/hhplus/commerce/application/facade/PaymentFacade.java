package io.hhplus.commerce.application.facade;

import io.hhplus.commerce.application.facade.usecase.PaymentUsecase;
import io.hhplus.commerce.application.service.member.MemberService;
import io.hhplus.commerce.application.service.order.OrderService;
import io.hhplus.commerce.application.service.order.PaymentService;
import io.hhplus.commerce.common.exception.CommerceErrorCodes;
import io.hhplus.commerce.common.exception.CommerceException;
import io.hhplus.commerce.domain.member.Point;
import io.hhplus.commerce.domain.order.Order;
import io.hhplus.commerce.domain.order.Payment;
import io.hhplus.commerce.presentation.controller.order.dto.PaymentRequestDto;
import io.hhplus.commerce.presentation.controller.order.dto.PaymentResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static io.hhplus.commerce.domain.order.OrderStatus.ORDERE_PENDING;

@Service
@RequiredArgsConstructor
public class PaymentFacade implements PaymentUsecase {
    private final PaymentService paymentService;
    private final MemberService memberService;
    private final OrderService orderService;

    @Override
    @Transactional
    public PaymentResponseDto payment(PaymentRequestDto dto) {
        Order order = orderService.getOrder(dto.orderId());
        Point point = memberService.getPointById(dto.memberId());

        // 주문 상태 확인
        if (order.getStatus() != ORDERE_PENDING) {
            throw new CommerceException(CommerceErrorCodes.INVALID_ORDER_STATUS);
        }

        // 잔액 차감 및 업데이트
        Point usedPoint = point.use(dto.amount());

        // 주문 상태 변경
        orderService.updateOrderState(order, ORDERE_PENDING);

        // 지불 정보 저장
        Payment payment = new Payment(order.getId(), dto.memberId(), dto.amount());
        paymentService.payment(payment);

        return new PaymentResponseDto(payment.getId(), payment.getMemberId(), payment.getOrderId(), payment.getAmount());
    }
}
