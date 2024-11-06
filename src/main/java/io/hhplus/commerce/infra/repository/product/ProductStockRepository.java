package io.hhplus.commerce.infra.repository.product;

import io.hhplus.commerce.domain.product.Product;
import io.hhplus.commerce.domain.product.ProductStock;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductStockRepository extends JpaRepository<ProductStock, Long> {
    @Override
    @Transactional
    @Lock(LockModeType.PESSIMISTIC_READ)
    Optional<ProductStock> findById(Long id);

    @Transactional
    @Lock(LockModeType.PESSIMISTIC_READ)
    List<ProductStock> findAllByIdIn(List<Long> productIds);
}
