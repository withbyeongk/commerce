package io.hhplus.commerce.application.service;

import io.hhplus.commerce.common.exception.CommerceErrorCodes;
import io.hhplus.commerce.common.exception.CommerceException;
import io.hhplus.commerce.domain.entity.Member;
import io.hhplus.commerce.domain.entity.Point;
import io.hhplus.commerce.infra.repository.MemberRepository;
import io.hhplus.commerce.infra.repository.PointRepository;
import io.hhplus.commerce.presentation.controller.member.dto.ChargePointDto;
import io.hhplus.commerce.presentation.controller.member.dto.PointResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PointRepository pointRepository;

    @Transactional
    public int chargePoint(ChargePointDto dto) {

        // 회원 id 조회
        Optional<Member> optionalMember = memberRepository.findById(dto.memberId());
        optionalMember.orElseThrow(() -> new CommerceException(CommerceErrorCodes.MEMBER_NOT_FOUND));

        // 현재 포인트 조회
        Optional<Point> optionalPoint = pointRepository.findById(dto.memberId());

        Point point = null;

        // 회원의 포인트가 존재하지 않을 경우 기본값으로 설정하여 저장
        if (!optionalPoint.isPresent()) {
            log.info("잔액 충전 내역 없음 : {}", dto.memberId());
            point = new Point(dto.memberId());
            pointRepository.save(point);
        }

        point = optionalPoint.get();

        // 포인트 업데이트
        point.charge(dto.points());
        pointRepository.save(point);

        // 회원 테이블에도 업데이트
        Member member = optionalMember.get();
        member.update(point.getPoint());
        memberRepository.save(member);

        return point.getPoint();
    }

    public PointResponseDto getPoint(Long memberId) {

        Optional<Member> optionalMember = memberRepository.findById(memberId);
        optionalMember.orElseThrow(() -> new CommerceException(CommerceErrorCodes.MEMBER_NOT_FOUND));

        return optionalMember.get().toResponseDto();
    }
}
