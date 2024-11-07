package io.hhplus.commerce.presentation.controller.member;

import io.hhplus.commerce.application.facade.MemberFacade;
import io.hhplus.commerce.application.facade.usecase.MemberUsecase;
import io.hhplus.commerce.application.service.member.MemberService;
import io.hhplus.commerce.presentation.controller.member.dto.ChargePointDto;
import io.hhplus.commerce.presentation.controller.member.dto.PointResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
@Tag(name = "/api/member", description = "회원 API")
public class MemberController {
    private final MemberUsecase memberUsecase;

    @Operation(summary = "잔액 충전")
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(mediaType = "application/json"))
    @PostMapping("/points")
    public PointResponseDto chargePoint(@RequestBody ChargePointDto dto) {
        return memberUsecase.chargePoint(dto);
    }

    @Operation(summary = "잔액 조회")
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PointResponseDto.class)))
    @GetMapping("/{memberId}/points")
    public PointResponseDto lookupPoint(@PathVariable (name = "memberId")Long memberId) {
        return memberUsecase.lookupPoint(memberId);
    }


}
