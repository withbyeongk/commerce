package io.hhplus.commerce.infra.repository;

import io.hhplus.commerce.domain.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
