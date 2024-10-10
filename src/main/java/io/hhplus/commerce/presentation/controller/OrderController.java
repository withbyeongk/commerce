package io.hhplus.commerce.presentation.controller;

import io.hhplus.commerce.presentation.dto.OrderRequestDto;
import io.hhplus.commerce.presentation.dto.OrderResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OrderController {
    private final OrderController orderController;

    @PostMapping("/order")
    public OrderResponseDto makeOrder(@RequestBody OrderRequestDto dto) {
        return new OrderResponseDto(1L, 2L, 3L, 1000);
    }

}
