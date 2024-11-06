package io.hhplus.commerce.application.facade;

import io.hhplus.commerce.application.facade.usecase.MemberUsecase;
import io.hhplus.commerce.application.service.member.MemberService;
import io.hhplus.commerce.common.exception.CommerceErrorCodes;
import io.hhplus.commerce.common.exception.CommerceException;
import io.hhplus.commerce.domain.member.Member;
import io.hhplus.commerce.domain.member.Point;
import io.hhplus.commerce.presentation.controller.member.dto.ChargePointDto;
import io.hhplus.commerce.presentation.controller.member.dto.PointResponseDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberFacade implements MemberUsecase {

    private final MemberService memberService;

    @Override
    public PointResponseDto lookupPoint(Long memberId) {
        return memberService.lookupMember(memberId).toPointResponseDto();
    }

    @Override
    @Transactional
    public PointResponseDto chargePoint(ChargePointDto dto) {
        Point chargedPoint = memberService.chargePoint(dto);

        memberService.updatePoint(chargedPoint.getMemberId(), chargedPoint.getPoint());

        return chargedPoint.toResponseDto();
    }
}
