package io.hhplus.commerce.domain.repository;

import io.hhplus.commerce.infra.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
}
