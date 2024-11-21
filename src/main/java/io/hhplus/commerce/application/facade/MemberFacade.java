package io.hhplus.commerce.application.facade;

import io.hhplus.commerce.application.facade.usecase.MemberUsecase;
import io.hhplus.commerce.application.service.member.MemberService;
import io.hhplus.commerce.presentation.controller.member.dto.ChargePointDto;
import io.hhplus.commerce.presentation.controller.member.dto.PointResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberFacade implements MemberUsecase {

    private final MemberService memberService;

    @Override
    public PointResponseDto lookupPoint(Long memberId) {
        return memberService.getPointById(memberId).toResponseDto();
    }

    @Override
    public PointResponseDto chargePoint(ChargePointDto dto) {
        return memberService.chargePoint(dto).toResponseDto();
    }
}
