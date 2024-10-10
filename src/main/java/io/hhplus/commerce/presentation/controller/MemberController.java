package io.hhplus.commerce.presentation.controller;

import io.hhplus.commerce.application.service.MemberService;
import io.hhplus.commerce.presentation.dto.ChargePointDto;
import io.hhplus.commerce.presentation.dto.PointResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/points")
    public void chargePoint(@RequestBody ChargePointDto dto) {

    }

    @GetMapping("/{memberId}")
    public PointResponseDto getPoint(@PathVariable (name = "memberId")Long memberId) {
        return new PointResponseDto(memberId, 100);
    }


}
