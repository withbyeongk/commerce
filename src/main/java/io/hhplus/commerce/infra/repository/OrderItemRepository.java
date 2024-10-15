package io.hhplus.commerce.infra.repository;

import io.hhplus.commerce.domain.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;


public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
