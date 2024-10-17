package io.hhplus.commerce.presentation.controller;

import io.hhplus.commerce.application.service.OrderService;
import io.hhplus.commerce.presentation.dto.OrderRequestDto;
import io.hhplus.commerce.presentation.dto.OrderResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/member/")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/{memberId}/order")
    public OrderResponseDto makeOrder(@RequestBody OrderRequestDto dto) {
        return orderService.makeOrder(dto);
    }

}
