package io.hhplus.commerce.presentation.controller;

import io.hhplus.commerce.application.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
}
