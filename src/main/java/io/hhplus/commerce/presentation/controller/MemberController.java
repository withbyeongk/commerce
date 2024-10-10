package io.hhplus.commerce.presentation.controller;

import io.hhplus.commerce.application.service.MemberService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/points")
    public void chargePoint(@RequestBody @Param("points") int point, @Param("member_id") Long memberId) {

    }

    @GetMapping("{memberId}")
    public int getPoint(@PathVariable Long memberId) {
        return 100;
    }


}
