package io.hhplus.commerce.infra.repository.order;

import io.hhplus.commerce.domain.member.Point;
import io.hhplus.commerce.domain.order.Payment;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
