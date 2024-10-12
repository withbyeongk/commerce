package io.hhplus.commerce.infra.repository;

import io.hhplus.commerce.infra.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
}
