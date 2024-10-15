package io.hhplus.commerce.infra.repository;

import io.hhplus.commerce.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MemberRepository extends JpaRepository<Member, Long> {
}
