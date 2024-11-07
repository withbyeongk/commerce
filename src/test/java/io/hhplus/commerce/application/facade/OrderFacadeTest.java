package io.hhplus.commerce.application.facade;

import io.hhplus.commerce.application.service.member.MemberService;
import io.hhplus.commerce.application.service.order.OrderService;
import io.hhplus.commerce.application.service.product.ProductService;
import io.hhplus.commerce.common.exception.CommerceErrorCodes;
import io.hhplus.commerce.common.exception.CommerceException;
import io.hhplus.commerce.domain.member.Member;
import io.hhplus.commerce.domain.member.Point;
import io.hhplus.commerce.domain.product.Product;
import io.hhplus.commerce.domain.product.ProductStock;
import io.hhplus.commerce.presentation.controller.order.dto.OrderRequestDto;
import io.hhplus.commerce.presentation.controller.order.dto.OrderResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class OrderFacadeTest {

    @InjectMocks
    private OrderFacade orderFacade;
    @Mock
    private MemberService memberService;
    @Mock
    private OrderService orderService;
    @Mock
    private ProductService productService;

    @Test
    @DisplayName("상품 주문 시 잔액부족으로 에러가 발생")
    void insufficientPointException() {

        // given
        Member member = new Member(1L, "member", 13999);
        Point point = new Point(1L, 13999);

        Product product1 = new Product(1L, "product1", 1000, 1, "description1");
        Product product2 = new Product(2L, "product2", 2000, 2, "description2");
        Product product3 = new Product(3L, "product3", 3000, 3, "description3");
        List<Product> products = Arrays.asList(product1, product2, product3);

        ProductStock productStock1 = new ProductStock(1L, 1);
        ProductStock productStock2 = new ProductStock(2L, 2);
        ProductStock productStock3 = new ProductStock(3L, 3);
        List<ProductStock> productStocks = Arrays.asList(productStock1, productStock2, productStock3);

        OrderRequestDto.OrderItemRequestDto requestDtoItem1 = new OrderRequestDto.OrderItemRequestDto(1L, 1);
        OrderRequestDto.OrderItemRequestDto requestDtoItem2 = new OrderRequestDto.OrderItemRequestDto(2L, 2);
        OrderRequestDto.OrderItemRequestDto requestDtoItem3 = new OrderRequestDto.OrderItemRequestDto(3L, 3);
        List<OrderRequestDto.OrderItemRequestDto> requestDtoItems = Arrays.asList(requestDtoItem1, requestDtoItem2, requestDtoItem3);

        OrderRequestDto requestDto = new OrderRequestDto(member.getId(), requestDtoItems);

        when(memberService.getPointById(anyLong())).thenReturn(point);
        when(productService.findAllByIds(anyList())).thenReturn(products);

        // when
        CommerceException e = assertThrows(CommerceException.class, () -> {
            orderFacade.makeOrder(requestDto);
        });

        // then
        assertEquals(CommerceErrorCodes.INSUFFICIENT_POINT.getMessage(), e.getMessage());

    }

    @Test
    @DisplayName("상품 주문 시 재고부족으로 에러 발생")
    void insufficientStockException() {
        // given
        Member member = new Member(1L, "member", 14000);
        Point point = new Point(1L, 14000);

        Product product1 = new Product(1L, "product1", 1000, 1, "description1");
        Product product2 = new Product(2L, "product2", 2000, 2, "description2");
        Product product3 = new Product(3L, "product3", 3000, 2, "description3");
        List<Product> products = Arrays.asList(product1, product2, product3);

        ProductStock productStock1 = new ProductStock(1L, 1);
        ProductStock productStock2 = new ProductStock(2L, 2);
        ProductStock productStock3 = new ProductStock(3L, 2);
        List<ProductStock> productStocks = Arrays.asList(productStock1, productStock2, productStock3);

        OrderRequestDto.OrderItemRequestDto requestDtoItem1 = new OrderRequestDto.OrderItemRequestDto(1L, 1);
        OrderRequestDto.OrderItemRequestDto requestDtoItem2 = new OrderRequestDto.OrderItemRequestDto(2L, 2);
        OrderRequestDto.OrderItemRequestDto requestDtoItem3 = new OrderRequestDto.OrderItemRequestDto(3L, 3);
        List<OrderRequestDto.OrderItemRequestDto> requestDtoItems = Arrays.asList(requestDtoItem1, requestDtoItem2, requestDtoItem3);

        OrderRequestDto requestDto = new OrderRequestDto(member.getId(), requestDtoItems);

        when(memberService.getPointById(anyLong())).thenReturn(point);
        when(productService.findAllByIds(anyList())).thenReturn(products);
        when(productService.findAllProductQuantityWithLock(anyList())).thenReturn(productStocks);

        // when
        CommerceException e = assertThrows(CommerceException.class, () -> {
            orderFacade.makeOrder(requestDto);
        });

        // then
        assertEquals(CommerceErrorCodes.INSUFFICIENT_STOCK.getMessage(), e.getMessage());
    }

    @Test
    @DisplayName("상품 주문 성공")
    void makeOrderTest() {
        // given
        Member member = new Member(1L, "member", 14000);
        Point point = new Point(1L, 14000);

        Product product1 = new Product(1L, "product1", 1000, 1, "description1");
        Product product2 = new Product(2L, "product2", 2000, 2, "description2");
        Product product3 = new Product(3L, "product3", 3000, 3, "description3");
        List<Product> products = Arrays.asList(product1, product2, product3);

        ProductStock productStock1 = new ProductStock(1L, 1);
        ProductStock productStock2 = new ProductStock(2L, 2);
        ProductStock productStock3 = new ProductStock(3L, 3);
        List<ProductStock> productStocks = Arrays.asList(productStock1, productStock2, productStock3);

        OrderRequestDto.OrderItemRequestDto requestDtoItem1 = new OrderRequestDto.OrderItemRequestDto(1L, 1);
        OrderRequestDto.OrderItemRequestDto requestDtoItem2 = new OrderRequestDto.OrderItemRequestDto(2L, 2);
        OrderRequestDto.OrderItemRequestDto requestDtoItem3 = new OrderRequestDto.OrderItemRequestDto(3L, 3);
        List<OrderRequestDto.OrderItemRequestDto> requestDtoItems = Arrays.asList(requestDtoItem1, requestDtoItem2, requestDtoItem3);

        OrderRequestDto requestDto = new OrderRequestDto(member.getId(), requestDtoItems);

        when(memberService.getPointById(anyLong())).thenReturn(point);
        when(productService.findAllByIds(anyList())).thenReturn(products);
        when(productService.findAllProductQuantityWithLock(anyList())).thenReturn(productStocks);

        // when
        OrderResponseDto result = orderFacade.makeOrder(requestDto);

        // then
        assertNotNull(result);
        assertEquals(requestDto.memberId(), result.memberId());
        assertEquals(14000, result.totalPrice());
    }
}
