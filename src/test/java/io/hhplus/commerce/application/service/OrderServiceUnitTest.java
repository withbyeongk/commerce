package io.hhplus.commerce.application.service;

import io.hhplus.commerce.application.service.order.OrderService;
import io.hhplus.commerce.common.exception.CommerceErrorCodes;
import io.hhplus.commerce.common.exception.CommerceException;
import io.hhplus.commerce.domain.member.Member;
import io.hhplus.commerce.domain.member.Point;
import io.hhplus.commerce.domain.order.OrderItem;
import io.hhplus.commerce.domain.product.Product;
import io.hhplus.commerce.domain.product.ProductStock;
import io.hhplus.commerce.infra.repository.member.MemberRepository;
import io.hhplus.commerce.infra.repository.member.PointRepository;
import io.hhplus.commerce.infra.repository.order.OrderItemRepository;
import io.hhplus.commerce.infra.repository.order.OrderRepository;
import io.hhplus.commerce.infra.repository.product.ProductRepository;
import io.hhplus.commerce.infra.repository.product.ProductStockRepository;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceUnitTest {

    @InjectMocks
    private OrderService orderService;
    @Mock
    private OrderItemRepository orderItemRepository;
    @Test
    @DisplayName("인기상품의 id 조회 테스트 성공")
    void testGetBestSellersProductIds() {
        // Given
        int days = 30;
        int limit = 5;
        List<OrderItem> items = Arrays.asList(
                new OrderItem(1L, 1L, 3)
                , new OrderItem(2L, 1L, 2)
                , new OrderItem(3L, 2L, 1));
        when(orderItemRepository.getOrderItemIdsByCreatedAtBetween(any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(items);

        // When
        List<Long> productIds = orderService.getBestSellersProductIds(days, limit);

        // Then
        assertThat(productIds).containsExactly(1L, 2L);
        verify(orderItemRepository, times(1)).getOrderItemIdsByCreatedAtBetween(any(LocalDateTime.class), any(LocalDateTime.class));
    }

}