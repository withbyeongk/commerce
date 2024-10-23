package io.hhplus.commerce.application.service;

import io.hhplus.commerce.domain.entity.Member;
import io.hhplus.commerce.domain.entity.Product;
import io.hhplus.commerce.domain.entity.ProductStock;
import io.hhplus.commerce.infra.repository.*;
import io.hhplus.commerce.presentation.dataflatform.ReportOrderInfo;
import io.hhplus.commerce.presentation.controller.order.dto.OrderRequestDto;
import io.hhplus.commerce.presentation.controller.order.dto.OrderResponseDto;
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
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
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

        // when & then
        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> orderService.makeOrder(dto));
        assertTrue(runtimeException.getMessage().contains("회원을 찾을 수 없습니다."));
    }

    @Test
    @DisplayName("주문 시 없는 상품이면 에러가 발생한다.")
    void InvalidProductIdInMakeOrder() {

        // given
        OrderRequestDto dto = prepareOrderRequestDto();
        when(memberRepository.findById(anyLong())).thenReturn(Optional.of(new Member()));
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when & then
        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> orderService.makeOrder(dto));
        assertTrue(runtimeException.getMessage().contains("찾을 수 없는 상품 ID"));
    }

    @Test
    @DisplayName("주문 시 회원의 잔액이 부족하면 에러가 발생한다.")
    void InsufficientPointsError() {

        // given
        OrderRequestDto dto = prepareOrderRequestDto();
        Member member = new Member(1L, "회원", 0, null, null, LocalDateTime.now());
        Product product = new Product(1L, "상품", 1000, 100, "상품설명", null, null, LocalDateTime.now());
        when(memberRepository.findById(anyLong())).thenReturn(Optional.of(member));
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));

        // when & then
        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> orderService.makeOrder(dto));
        assertTrue(runtimeException.getMessage().contains("잔액 부족"));
    }

    @Test
    @DisplayName("주문 시 상품 재고가 부족하면 에러가 발생한다.")
    void InsufficientStock() {

        // given
        OrderRequestDto dto = prepareOrderRequestDto();
        Member member = new Member(1L, "회원", 1000, null, null, LocalDateTime.now());
        Product product = new Product(1L, "상품", 1000, 0, "상품설명", null, null, LocalDateTime.now());
        ProductStock productStock = new ProductStock(1L, 0);
        when(memberRepository.findById(anyLong())).thenReturn(Optional.of(member));
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        when(productStockRepository.findById(anyLong())).thenReturn(Optional.of(productStock));

        // when & then
        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> orderService.makeOrder(dto));
        assertTrue(runtimeException.getMessage().contains("상품 재고 부족"));
    }

    @Test
    @DisplayName("주문 성공 시 주문정보를 반환합니다.")
    void makeOrderSuccessTest() {

        // given
        OrderRequestDto dto = prepareOrderRequestDto();
        Member member = new Member(dto.memberId(), "회원", 10000, null, null, LocalDateTime.now());
        Product product = new Product(1L, "상품", 1000, 0, "상품설명", null, null, LocalDateTime.now());
        ProductStock productStock = new ProductStock(1L, 10);
        when(memberRepository.findById(anyLong())).thenReturn(Optional.of(member));
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        when(productStockRepository.findById(anyLong())).thenReturn(Optional.of(productStock));
        when(orderRepository.save(org.mockito.ArgumentMatchers.any())).thenAnswer(i -> i.getArguments()[0]);
        when(orderItemRepository.save(org.mockito.ArgumentMatchers.any())).thenAnswer(i -> i.getArguments()[0]);

        // when
        OrderResponseDto result = orderService.makeOrder(dto);

        // then
        assertNotNull(result);
        assertEquals(product.getPrice(), result.totalPrice());
    }

}