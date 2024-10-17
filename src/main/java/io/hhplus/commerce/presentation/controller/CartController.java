package io.hhplus.commerce.presentation.controller;

import io.hhplus.commerce.application.service.CartService;
import io.hhplus.commerce.presentation.dto.CartPutInDto;
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

    @PostMapping("/{memberId}/cart")
    public void putIn(@RequestBody CartPutInDto dto) {
        cartService.putIn(dto);
    }

    @DeleteMapping("/cart/{cartId}")
    public void putOff(@PathVariable (name = "cartId")Long cartId) {
        cartService.putOff(cartId);
    }

    @GetMapping("/{memberId}/cart")
    public List<ProductResponseDto> getCart(@PathVariable (name = "memberId")Long memberId) {
        return cartService.getProductsInCart(memberId);
    }

    @PatchMapping("/{memberId}cart/products")
    public void changeQuantity(@RequestBody ChangeQuantityDto dto) {
        cartService.changeQuantity(dto);
    }
}
