package io.hhplus.commerce.infra.repository;

import io.hhplus.commerce.domain.entity.Point;
import io.hhplus.commerce.domain.entity.ProductStock;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface PointRepository extends JpaRepository<Point, Long> {

    @Override
    @Transactional
    @Lock(LockModeType.OPTIMISTIC)
    Optional<Point> findById(Long id);
}
