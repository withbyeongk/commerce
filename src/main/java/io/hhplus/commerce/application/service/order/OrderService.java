package io.hhplus.commerce.application.service.order;

import io.hhplus.commerce.common.exception.CommerceErrorCodes;
import io.hhplus.commerce.common.exception.CommerceException;
import io.hhplus.commerce.domain.order.Order;
import io.hhplus.commerce.domain.order.OrderItem;
import io.hhplus.commerce.domain.order.OrderStatus;
import io.hhplus.commerce.infra.repository.order.OrderItemRepository;
import io.hhplus.commerce.infra.repository.order.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    public List<Long> getBestSellersProductIds(int day, int limit) {
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = LocalDateTime.now().minusDays(day);
        List<OrderItem> items = orderItemRepository.getOrderItemIdsByCreatedAtBetween(startDate, endDate);

        Map<Long, Long> itemCountMap = items.stream()
                .collect(Collectors.groupingBy(
                        OrderItem::getProductId,
                        Collectors.counting()
                ));

        return itemCountMap.entrySet().stream()
                .sorted(Map.Entry.<Long, Long>comparingByValue().reversed())
                .limit(limit)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public OrderItem addOrderItem(OrderItem item) {
        return orderItemRepository.save(item);
    }

    public Order addOrder(Order order) {
        return orderRepository.save(order);
    }

    public Order getOrder(Long id) {
        return orderRepository.findById(id).orElseThrow(()-> new CommerceException(CommerceErrorCodes.ORDER_NOT_FOUND));
    }

    public Order updateOrderState(Order order, OrderStatus status) {
        order.updateStatus(status);
        return orderRepository.save(order);
    }

}
