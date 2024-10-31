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
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
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
    @Retryable(
            retryFor = ObjectOptimisticLockingFailureException.class,
            maxAttempts = 5,
            backoff = @Backoff(delay = 10),
            recover = "recoverFailure"
    )
    public int chargePoint(ChargePointDto dto) {

        int resultPoint = 0;

        // 회원 id 조회
        Member member = memberRepository.findById(dto.memberId()).orElseThrow(() -> new CommerceException(CommerceErrorCodes.MEMBER_NOT_FOUND));

        // 현재 포인트 조회
        Optional<Point> optionalPoint = pointRepository.findById(dto.memberId());

        Point point = null;

        // 회원의 포인트가 존재하지 않을 경우 기본값으로 설정하여 저장
        if (!optionalPoint.isPresent()) {
            log.info("잔액 충전 내역 없음 : {}", dto.memberId());
            point = new Point(dto.memberId());
            pointRepository.save(point);
        } else {
            point = optionalPoint.get();
        }

        // 포인트 업데이트
        point.charge(dto.points());
        pointRepository.save(point);

        // 회원 테이블에도 업데이트
        member.update(point.getPoint());
        memberRepository.save(member);
        resultPoint = point.getPoint();

        return resultPoint;
    }

    @Recover
    public int recoverFailure(ObjectOptimisticLockingFailureException e) {
        throw new CommerceException(CommerceErrorCodes.OPTIMISTIC_LOCKING_FAILURE);
    }



    public PointResponseDto getPoint(Long memberId) {

        Optional<Member> optionalMember = memberRepository.findById(memberId);
        optionalMember.orElseThrow(() -> new CommerceException(CommerceErrorCodes.MEMBER_NOT_FOUND));

        return optionalMember.get().toResponseDto();
    }
}
