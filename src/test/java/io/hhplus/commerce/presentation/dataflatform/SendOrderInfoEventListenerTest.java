package io.hhplus.commerce.presentation.dataflatform;

import io.hhplus.commerce.application.facade.PaymentFacade;
import io.hhplus.commerce.application.service.member.MemberService;
import io.hhplus.commerce.application.service.order.OrderService;
import io.hhplus.commerce.application.service.order.PaymentService;
import io.hhplus.commerce.domain.member.Point;
import io.hhplus.commerce.domain.order.Order;
import io.hhplus.commerce.domain.order.Payment;
import io.hhplus.commerce.presentation.controller.order.dto.PaymentRequestDto;
import io.hhplus.commerce.presentation.controller.order.dto.PaymentResponseDto;
import io.hhplus.commerce.presentation.dataflatform.event.OrderCompletedEvent;
import io.hhplus.commerce.presentation.dataflatform.event.SendOrderInfoEventListener;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SendOrderInfoEventListenerTest {

    @Mock
    private ReportOrderInfo reportOrderInfo;
    @Mock
    private ApplicationEventPublisher eventPublisher;
    @Mock
    private PaymentService paymentService;
    @Mock
    private MemberService memberService;
    @Mock
    private OrderService orderService;

    @InjectMocks
    private PaymentFacade paymentFacade;

    @InjectMocks
    private SendOrderInfoEventListener listener;

    @Autowired
    private ApplicationEventPublisher publisher;

    @Test
    @DisplayName("결제 시 이벤트가 발행하는 테스트")
    void whenPaymentThenEventPublish() throws InterruptedException {
        // given
        PaymentRequestDto requestDto = new PaymentRequestDto(1L, 1L, 1000);

        when(orderService.getOrder(any())).thenReturn(new Order(1L, 1L, 1000));
        when(memberService.getPointById(any())).thenReturn(new Point(1L, 10000));
        when(paymentService.payment(any())).thenReturn(new Payment(1L, 1L, 1000));

        // when
        PaymentResponseDto payment = paymentFacade.payment(requestDto);

        // then
        ArgumentCaptor<OrderCompletedEvent> argument = ArgumentCaptor.forClass(OrderCompletedEvent.class);
        Mockito.verify(eventPublisher, times(1)).publishEvent(argument.capture());

        assertEquals(payment.orderId(), argument.getValue().getOrderId());
    }

    @Test
    @DisplayName("이벤트 발행 시 주문정보 전송")
    public void whenEventThensendOrderInfo() {
        // given
        OrderCompletedEvent event = new OrderCompletedEvent(this, 1L, 1000);

        // when
        listener.onSendOrderInfoEvent(event);

        // then
        verify(reportOrderInfo, times(1)).sendOrderInfomation(event);
    }
}