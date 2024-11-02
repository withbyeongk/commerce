package io.hhplus.commerce.application.service;

import io.hhplus.commerce.common.exception.CommerceErrorCodes;
import io.hhplus.commerce.common.exception.CommerceException;
import io.hhplus.commerce.domain.entity.Member;
import io.hhplus.commerce.domain.entity.Point;
import io.hhplus.commerce.domain.entity.Product;
import io.hhplus.commerce.domain.entity.ProductStock;
import io.hhplus.commerce.infra.repository.*;
import io.hhplus.commerce.presentation.controller.order.dto.OrderRequestDto;
import io.hhplus.commerce.presentation.controller.order.dto.OrderResponseDto;
import io.hhplus.commerce.presentation.dataflatform.ReportOrderInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceUnitTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductStockRepository productStockRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private PointRepository pointRepository;

    @Mock
    private ReportOrderInfo reportOrderInfo;

    @BeforeEach
    void setup() {

    }
    private OrderRequestDto prepareOrderRequestDto() {
        Long memberId = 1L;

        List<OrderRequestDto.OrderItemRequestDto> products = Arrays.asList(
                new OrderRequestDto.OrderItemRequestDto(1L, 1)
        );
        return new OrderRequestDto(memberId, products);
    }

    @Test
    @DisplayName("주문 시 없는 회원이면 에러가 발생한다.")
    void InvalidMemberIdInMakeOrder() {

        // given
        OrderRequestDto dto = prepareOrderRequestDto();
        when(memberRepository.findById(anyLong())).thenReturn(Optional.empty());

        // expected
        CommerceException e = assertThrows(CommerceException.class, () -> {
            orderService.makeOrder(dto);
        });

        // then
        verify(memberRepository).findById(anyLong());
        assertEquals(CommerceErrorCodes.MEMBER_NOT_FOUND, e.getErrorCode());

    }

    @Test
    @DisplayName("주문 시 없는 상품이면 에러가 발생한다.")
    void InvalidProductIdInMakeOrder() {

        // given
        OrderRequestDto dto = prepareOrderRequestDto();
        when(memberRepository.findById(anyLong())).thenReturn(Optional.of(new Member()));
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(pointRepository.findById(null)).thenReturn(Optional.empty());

        // expected
        CommerceException e = assertThrows(CommerceException.class, () -> {
            orderService.makeOrder(dto);
        });

        // then
        assertEquals(e.getErrorCode(), CommerceErrorCodes.PRODUCT_NOT_FOUND);
    }

    @Test
    @DisplayName("주문 시 회원의 잔액이 부족하면 에러가 발생한다.")
    void InsufficientPointsError() {

        // given
        OrderRequestDto dto = prepareOrderRequestDto();
        Member member = new Member(1L, "회원", 0);
        Product product = new Product(1L, "상품", 1000, 100, "상품설명");
        when(memberRepository.findById(anyLong())).thenReturn(Optional.of(member));
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));

        // expected
        CommerceException runtimeException = assertThrows(CommerceException.class, () -> {
            orderService.makeOrder(dto);
        });

        // then
        assertEquals(CommerceErrorCodes.INSUFFICIENT_POINT, runtimeException.getErrorCode());
    }

    @Test
    @DisplayName("주문 시 상품 재고가 부족하면 에러가 발생한다.")
    void InsufficientStock() {

        // given
        OrderRequestDto dto = prepareOrderRequestDto();
        Member member = new Member(1L, "회원", 1000);
        Product product = new Product(1L, "상품", 1000, 0, "상품설명");
        Point point = new Point(1L, 1000);
        ProductStock productStock = new ProductStock(1L, 0);
        when(memberRepository.findById(anyLong())).thenReturn(Optional.of(member));
        when(pointRepository.findById(anyLong())).thenReturn(Optional.of(point));
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        when(productStockRepository.findById(anyLong())).thenReturn(Optional.of(productStock));

        // expected
        CommerceException e = assertThrows(CommerceException.class, () -> {
            orderService.makeOrder(dto);
        });

        // then
        assertEquals(e.getErrorCode(), CommerceErrorCodes.INSUFFICIENT_STOCK);
    }

    @Test
    @DisplayName("주문 성공 시 주문정보를 반환합니다.")
    void makeOrderSuccessTest() {

        // given
        OrderRequestDto dto = prepareOrderRequestDto();
        Member member = new Member(dto.memberId(), "회원", 10000);
        Point point = new Point(1L, 1000);
        Product product = new Product(1L, "상품", 1000, 0, "상품설명");
        ProductStock productStock = new ProductStock(1L, 10);
        when(memberRepository.findById(anyLong())).thenReturn(Optional.of(member));
        when(pointRepository.findById(anyLong())).thenReturn(Optional.of(point));
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        when(productStockRepository.findById(anyLong())).thenReturn(Optional.of(productStock));
        when(orderRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);
        when(orderItemRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        // when
        OrderResponseDto result = orderService.makeOrder(dto);

        // then
        assertNotNull(result);
        assertEquals(product.getPrice(), result.totalPrice());
    }

}