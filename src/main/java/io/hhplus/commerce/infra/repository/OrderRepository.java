package io.hhplus.commerce.infra.repository;

import io.hhplus.commerce.domain.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;


public interface OrderRepository extends JpaRepository<Order, Long> {
}
