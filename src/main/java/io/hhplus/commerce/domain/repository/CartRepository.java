package io.hhplus.commerce.domain.repository;

import io.hhplus.commerce.infra.entity.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<CartEntity, Long> {
}
