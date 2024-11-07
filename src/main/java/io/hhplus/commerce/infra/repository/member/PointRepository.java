package io.hhplus.commerce.infra.repository.member;

import io.hhplus.commerce.domain.member.Point;
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
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Point> findById(Long id);
}
