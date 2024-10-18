package io.hhplus.commerce.presentation.controller;

import io.hhplus.commerce.application.service.OrderService;
import io.hhplus.commerce.presentation.dto.OrderRequestDto;
import io.hhplus.commerce.presentation.dto.OrderResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/member/")
@RequiredArgsConstructor
@Tag(name = "/api/member/", description = "주문 API")
public class OrderController {
    private final OrderService orderService;

    @Operation(summary = "상품 주문")
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderResponseDto.class)))
    @PostMapping("/{memberId}/order")
    public OrderResponseDto makeOrder(@RequestBody OrderRequestDto dto) {
        return orderService.makeOrder(dto);
    }

}
