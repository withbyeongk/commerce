package io.hhplus.commerce.application.facade.usecase;

import io.hhplus.commerce.presentation.controller.member.dto.ChargePointDto;
import io.hhplus.commerce.presentation.controller.member.dto.PointResponseDto;

public interface MemberUsecase {

    PointResponseDto lookupPoint(Long memberId);
    PointResponseDto chargePoint(ChargePointDto dto);
}
