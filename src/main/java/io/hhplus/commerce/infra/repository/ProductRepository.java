package io.hhplus.commerce.infra.repository;

import io.hhplus.commerce.domain.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProductRepository extends JpaRepository<Product, Long> {
}
