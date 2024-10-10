package io.hhplus.commerce.presentation.controller;

import io.hhplus.commerce.presentation.dto.PayRequestDTO;
import io.hhplus.commerce.presentation.dto.PayResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
public class PayController {
    private final PayController payController;

    @PostMapping("/pay")
    public PayResponseDTO pay(@RequestBody PayRequestDTO dto) {
        return new PayResponseDTO(1L);
    }
}
