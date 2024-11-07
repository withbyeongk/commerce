package io.hhplus.commerce.application.facade;

import io.hhplus.commerce.application.facade.usecase.OrderUsecase;
import io.hhplus.commerce.application.service.member.MemberService;
import io.hhplus.commerce.application.service.order.OrderService;
import io.hhplus.commerce.application.service.product.ProductService;
import io.hhplus.commerce.common.exception.CommerceErrorCodes;
import io.hhplus.commerce.common.exception.CommerceException;
import io.hhplus.commerce.domain.member.Member;
import io.hhplus.commerce.domain.member.Point;
import io.hhplus.commerce.domain.order.Order;
import io.hhplus.commerce.domain.order.OrderItem;
import io.hhplus.commerce.domain.product.Product;
import io.hhplus.commerce.domain.product.ProductStock;
import io.hhplus.commerce.presentation.controller.order.dto.OrderRequestDto;
import io.hhplus.commerce.presentation.controller.order.dto.OrderResponseDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderFacade implements OrderUsecase {
    private final MemberService memberService;
    private final OrderService orderService;
    private final ProductService productService;

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public OrderResponseDto makeOrder(OrderRequestDto dto) {
        Member member = validateMember(dto.memberId());
        List<OrderRequestDto.OrderItemRequestDto> items = dto.products();

        List<Long> productIds = items.stream()
                .map(OrderRequestDto.OrderItemRequestDto::productId)
                .collect(Collectors.toList());

        Map<Long, Integer> productMap = items.stream()
                .collect(toMap(OrderRequestDto.OrderItemRequestDto::productId
                        , OrderRequestDto.OrderItemRequestDto::quantity));

        // 총액 계산
        List<Product> products = productService.findAllByIds(productIds);
        int totalPrice = products.stream()
                .mapToInt(product -> product.getPrice() * productMap.get(product.getId()))
                .sum();

        // 잔액 확인
        Point point = memberService.getPointById(dto.memberId());
        if (point.getPoint() < totalPrice) {
            throw new CommerceException(CommerceErrorCodes.INSUFFICIENT_POINT);
        }

        // 상품 재고 조회 ids로
        List<ProductStock> productStocks = productService.findAllProductQuantityWithLock(productIds);

        // 상품 재고가 모두 충분한지 확인(productId가 같을 때 stock이 충분한지 확인)
        for (ProductStock stock : productStocks) {
            if (stock.getStock() < productMap.get(stock.getId())) {
                throw new CommerceException(CommerceErrorCodes.INSUFFICIENT_STOCK);
            }
        }

        // 주문 등록
        Order order = new Order(dto.memberId(), totalPrice);
        orderService.addOrder(order);

        for (OrderRequestDto.OrderItemRequestDto orderItemDto : dto.products()) {
            // 상품 재고 업데이트
            productService.updateStock(orderItemDto);

            // 아이템 등록
            OrderItem item = new OrderItem(order.getId(), orderItemDto.productId(), orderItemDto.quantity());
            orderService.addOrderItem(item);
        }

        return new OrderResponseDto(order.getId(), dto.memberId(), totalPrice);
    }

    private Member validateMember(Long memberId) {
        return memberService.lookupMember(memberId);
    }


}
