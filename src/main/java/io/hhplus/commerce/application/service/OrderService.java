package io.hhplus.commerce.application.service;

import io.hhplus.commerce.domain.entity.*;
import io.hhplus.commerce.infra.repository.*;
import io.hhplus.commerce.presentation.dto.OrderRequestDto;
import io.hhplus.commerce.presentation.dto.OrderResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;
    private final ProductStockRepository productStockRepository;

    @Transactional(rollbackFor = {Exception.class})
    public OrderResponseDto makeOrder(OrderRequestDto dto) {

        Member member = memberRepository.findById(dto.memberId())
                .orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다."));

        int memberPoint = member.getPoint();

        List<Product> products = dto.products().stream()
                .map(orderItemDto -> productRepository.findById(orderItemDto.productId()).orElseThrow(
                        () -> new RuntimeException("찾을 수 없는 상품 ID : " + orderItemDto.productId())))
                .collect(Collectors.toList());

        int totalPrice = dto.products().stream()
                .mapToInt(orderItemDto -> orderItemDto.amount() * products.stream()
                        .filter(product -> product.getId().equals(orderItemDto.productId()))
                        .findFirst().get().getPrice())
                .sum();

        if (totalPrice > memberPoint) {
            throw new RuntimeException("잔액 부족으로 구매할 수 없습니다.");
        }

        // 상품들 재고 확인
        for (OrderRequestDto.OrderItemRequestDto orderItemDto : dto.products()) {
            ProductStock productStock = productStockRepository.findById(orderItemDto.productId()).get();

            if (productStock.getStock() < orderItemDto.amount()) {
                throw new RuntimeException("상품 재고 부족: " + orderItemDto.productId());
            }
        }

        // 잔액 감소
        member.use(totalPrice);

        // 주문 등록
        Order order = new Order(member.getId(), totalPrice, LocalDateTime.now());
        Order savedOrder = orderRepository.save(order);

        for (OrderRequestDto.OrderItemRequestDto orderItemDto : dto.products()) {
            ProductStock productStock = productStockRepository.findById(orderItemDto.productId()).get();

            // 재고 감소
            productStock.minus(orderItemDto.amount());
            productStockRepository.save(productStock);

            // 주문 상품 등록
            orderItemRepository.save(new OrderItem(savedOrder.getId(), orderItemDto.productId(), orderItemDto.productId(), orderItemDto.amount()));
        }


        // 주문 정보 반환
        List<OrderResponseDto.OrderItemRequestDto> orderItems = dto.products().stream()
                .map(item -> new OrderResponseDto.OrderItemRequestDto(item.productId(), item.amount()))
                .collect(Collectors.toList());

        OrderResponseDto responseDto = new OrderResponseDto(order.getId(), order.getMemberId(), order.getTotalPrice(), orderItems);

        return responseDto;
    }

}
