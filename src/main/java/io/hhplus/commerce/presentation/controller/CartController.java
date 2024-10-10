package io.hhplus.commerce.presentation.controller;

import io.hhplus.commerce.application.service.CartService;
import io.hhplus.commerce.presentation.dto.ChangeQuantityDto;
import io.hhplus.commerce.presentation.dto.ProductResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @GetMapping("/cart/products")
    public void putIn() {

    }

    @DeleteMapping("/cart/products")
    public void putOff() {

    }

    @GetMapping("/{memberId}/cart/products")
    public List<ProductResponseDto> getCart(@PathVariable (name = "memberId")Long memberId) {
        return List.of(
                new ProductResponseDto(1L, "Product 1", 100, 50, "Description 1", null, LocalDateTime.now(), LocalDateTime.now()),
                new ProductResponseDto(2L, "Product 2", 200, 49, "Description 2", null, LocalDateTime.now(), LocalDateTime.now()));
    }

    @PatchMapping("/{memberId}/cart/products")
    public void changeQuantity(@RequestBody ChangeQuantityDto dto) {

    }
}
