package io.hhplus.commerce.application.service.member;

import io.hhplus.commerce.common.exception.CommerceErrorCodes;
import io.hhplus.commerce.common.exception.CommerceException;
import io.hhplus.commerce.domain.member.Member;
import io.hhplus.commerce.domain.member.Point;
import io.hhplus.commerce.infra.repository.member.MemberRepository;
import io.hhplus.commerce.infra.repository.member.PointRepository;
import io.hhplus.commerce.presentation.controller.member.dto.ChargePointDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PointRepository pointRepository;


    public Point chargePoint(ChargePointDto dto) {
        Point point = getPointById(dto.memberId());

        point.charge(dto.points());

        return pointRepository.save(point);
    }

    public Member updatePoint(Long memberId, int point) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new CommerceException(CommerceErrorCodes.MEMBER_NOT_FOUND));

        member.update(point);

        return memberRepository.save(member);
    }

    public Member updatePoint(Point point) {
        Member member = memberRepository.findById(point.getMemberId()).orElseThrow(() -> new CommerceException(CommerceErrorCodes.MEMBER_NOT_FOUND));
        member.update(point.getPoint());
        pointRepository.save(point);
        return memberRepository.save(member);
    }


    public Point getPointById(Long memberId) {
        Optional<Point> optionalPoint = pointRepository.findById(memberId);

        // 회원의 포인트가 존재하지 않을 경우 기본값으로 설정하여 저장
        if (optionalPoint.isEmpty()) {
            log.info("잔액 충전 내역 없음 : {}", memberId);
            return pointRepository.save(new Point(memberId));
        }

        return optionalPoint.get();
    }


    public Member lookupMember(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new CommerceException(CommerceErrorCodes.MEMBER_NOT_FOUND));
    }

}
