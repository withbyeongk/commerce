package io.hhplus.commerce.domain.repository;

import io.hhplus.commerce.infra.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
}
