package io.hhplus.commerce.application.service;

import io.hhplus.commerce.common.exception.CommerceErrorCodes;
import io.hhplus.commerce.common.exception.CommerceException;
import io.hhplus.commerce.domain.entity.*;
import io.hhplus.commerce.infra.repository.*;
import io.hhplus.commerce.presentation.controller.order.dto.OrderRequestDto;
import io.hhplus.commerce.presentation.controller.order.dto.OrderResponseDto;
import io.hhplus.commerce.presentation.dataflatform.ReportOrderInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;
    private final ProductStockRepository productStockRepository;
    private final ReportOrderInfo reportOrderInfo;
    private final PointRepository pointRepository;

    @Transactional(rollbackFor = {Exception.class})
    public OrderResponseDto makeOrder(OrderRequestDto dto) {

        Member member = memberRepository.findById(dto.memberId())
                .orElseThrow(() -> new CommerceException(CommerceErrorCodes.MEMBER_NOT_FOUND));

        int memberPoint = 0;

        Optional<Point> optionalPoint = pointRepository.findById(member.getId());
        if (optionalPoint.isEmpty()) {
            Point point = new Point(member.getId());
            pointRepository.save(point);
        } else {
            memberPoint = optionalPoint.get().getPoint();
        }

        List<Product> products = dto.products().stream()
                .map(orderItemDto -> productRepository.findById(orderItemDto.productId()).orElseThrow(
                        () -> new CommerceException(CommerceErrorCodes.PRODUCT_NOT_FOUND)))
                .collect(Collectors.toList());

        int totalPrice = dto.products().stream()
                .mapToInt(orderItemDto -> orderItemDto.amount() * products.stream()
                        .filter(product -> product.getId().equals(orderItemDto.productId()))
                        .findFirst().get().getPrice())
                .sum();

        if (totalPrice > memberPoint) {
            throw new CommerceException(CommerceErrorCodes.INSUFFICIENT_POINT);
        }

        // 상품들 재고 확인
        for (OrderRequestDto.OrderItemRequestDto orderItemDto : dto.products()) {
            ProductStock productStock = productStockRepository.findById(orderItemDto.productId()).get();

            if (productStock.getStock() < orderItemDto.amount()) {
                throw new CommerceException(CommerceErrorCodes.INSUFFICIENT_STOCK);
            }
        }

        // 잔액 감소
        Point point = optionalPoint.get();
        point.use(totalPrice);
        pointRepository.save(point);

        member.update(point.getPoint());
        memberRepository.save(member);



        // 주문 등록
        Order order = new Order(null, member.getId(), totalPrice, null, null, LocalDateTime.now());
        Order savedOrder = orderRepository.save(order);

        for (OrderRequestDto.OrderItemRequestDto orderItemDto : dto.products()) {
            ProductStock productStock = productStockRepository.findById(orderItemDto.productId()).get();
            Product product = productRepository.findById(orderItemDto.productId()).get();

            // 재고 감소
            productStock.minus(orderItemDto.amount());
            productStockRepository.save(productStock);

            // 상품 테이블 업데이트
            productRepository.save(product.minusStock(orderItemDto.amount()));

            // 주문 상품 등록
            orderItemRepository.save(new OrderItem(savedOrder.getId(), orderItemDto.productId(), orderItemDto.productId(), orderItemDto.amount()));
        }


        // 주문 정보 반환
        List<OrderResponseDto.OrderItemRequestDto> orderItems = dto.products().stream()
                .map(item -> new OrderResponseDto.OrderItemRequestDto(item.productId(), item.amount()))
                .collect(Collectors.toList());

        OrderResponseDto responseDto = new OrderResponseDto(order.getId(), order.getMemberId(), order.getTotalPrice(), orderItems);

        reportOrderInfo.sendOrderInfomation(responseDto.toOrderInfoDto());

        return responseDto;
    }

}
