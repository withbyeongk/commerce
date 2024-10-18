package io.hhplus.commerce.presentation.controller;

import io.hhplus.commerce.application.service.MemberService;
import io.hhplus.commerce.presentation.dto.ChargePointDto;
import io.hhplus.commerce.presentation.dto.PointResponseDto;
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
    private final MemberService memberService;

    @Operation(summary = "잔액 조회")
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(mediaType = "application/json"))
    @PostMapping("/points")
    public void chargePoint(@RequestBody ChargePointDto dto) {
        memberService.chargePoint(dto);
    }

    @Operation(summary = "잔액 충전")
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PointResponseDto.class)))
    @GetMapping("/{memberId}/points")
    public PointResponseDto getPoint(@PathVariable (name = "memberId")Long memberId) {
        return memberService.getPoint(memberId);
    }


}
