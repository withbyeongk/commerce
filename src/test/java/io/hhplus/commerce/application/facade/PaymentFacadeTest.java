package io.hhplus.commerce.application.facade;

import io.hhplus.commerce.application.service.member.MemberService;
import io.hhplus.commerce.application.service.order.OrderService;
import io.hhplus.commerce.application.service.order.PaymentService;
import io.hhplus.commerce.common.exception.CommerceErrorCodes;
import io.hhplus.commerce.common.exception.CommerceException;
import io.hhplus.commerce.domain.member.Point;
import io.hhplus.commerce.domain.order.Order;
import io.hhplus.commerce.domain.order.OrderStatus;
import io.hhplus.commerce.domain.order.Payment;
import io.hhplus.commerce.presentation.controller.order.dto.PaymentRequestDto;
import io.hhplus.commerce.presentation.controller.order.dto.PaymentResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentFacadeTest {
    @InjectMocks
    private PaymentFacade paymentFacade;
    @Mock
    private PaymentService paymentService;
    @Mock
    private MemberService memberService;
    @Mock
    private OrderService orderService;

    @Test
    @DisplayName("이미 처리된 주문일 경우 유효하지 않은 주문 상태 오류 발생")
    public void invalidOrderStatusException() {
        // given
        PaymentRequestDto requestDto = new PaymentRequestDto(1L, 1L, 1000);
        Order order = new Order(1L, 1000, OrderStatus.COMPLETED);

        when(orderService.getOrder(any())).thenReturn(order);

        // when
        CommerceException e = assertThrows(CommerceException.class, () -> {
            paymentFacade.payment(requestDto);
        });

        // then
        assertEquals(CommerceErrorCodes.INVALID_ORDER_STATUS, e.getErrorCode());
    }

    @Test
    @DisplayName("결제 성공")
    public void paymentTest() {
        // given
        PaymentRequestDto requestDto = new PaymentRequestDto(1L, 1L, 1000);
        Order order = new Order(1L, 1000);
        Point point = new Point(1L, 2000);

        when(orderService.getOrder(any())).thenReturn(order);
        when(memberService.getPointById(any())).thenReturn(point);
        when(paymentService.payment(any(Payment.class))).thenReturn(new Payment(1L, 1L, 1L, 1000));

        // when
        PaymentResponseDto responseDto = paymentFacade.payment(requestDto);

        // then
        assertNotNull(responseDto);
        verify(memberService, times(1)).updatePoint(any(Point.class));
        verify(orderService, times(1)).updateOrderState(any(Order.class), any(OrderStatus.class));
        verify(paymentService, times(1)).payment(any(Payment.class));
    }

}