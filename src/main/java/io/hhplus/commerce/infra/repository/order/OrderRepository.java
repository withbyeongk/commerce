package io.hhplus.commerce.infra.repository.order;

import io.hhplus.commerce.domain.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
