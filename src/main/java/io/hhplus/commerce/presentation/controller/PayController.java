package io.hhplus.commerce.presentation.controller;

import io.hhplus.commerce.application.service.PayService;
import io.hhplus.commerce.presentation.dto.PayRequestDto;
import io.hhplus.commerce.presentation.dto.PayResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
public class PayController {
    private final PayService payService;

    @PostMapping("/pay")
    public PayResponseDto pay(@RequestBody PayRequestDto dto) {
        return new PayResponseDto(1L);
    }
}
