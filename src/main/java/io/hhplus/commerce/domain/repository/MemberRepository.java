package io.hhplus.commerce.domain.repository;

import io.hhplus.commerce.infra.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
}
