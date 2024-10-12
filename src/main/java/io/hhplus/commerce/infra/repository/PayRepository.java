package io.hhplus.commerce.infra.repository;

import io.hhplus.commerce.infra.entity.PayEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PayRepository extends JpaRepository<PayEntity, Long> {
}
