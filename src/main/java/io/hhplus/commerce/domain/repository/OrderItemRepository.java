package io.hhplus.commerce.domain.repository;

import io.hhplus.commerce.infra.entity.OrderItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface OrderItemRepository extends JpaRepository<OrderItemEntity, Long> {
}
