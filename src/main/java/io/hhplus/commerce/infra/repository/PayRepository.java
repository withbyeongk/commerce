package io.hhplus.commerce.infra.repository;

import io.hhplus.commerce.domain.entity.Pay;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PayRepository extends JpaRepository<Pay, Long> {
}
