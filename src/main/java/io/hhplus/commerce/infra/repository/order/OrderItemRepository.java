package io.hhplus.commerce.infra.repository.order;

import io.hhplus.commerce.domain.order.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> getOrderItemIdsByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
}
