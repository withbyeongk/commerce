package io.hhplus.commerce.presentation.controller.cart;

import io.hhplus.commerce.application.service.CartService;
import io.hhplus.commerce.presentation.controller.cart.dto.CartPutInDto;
import io.hhplus.commerce.presentation.controller.cart.dto.ChangeQuantityDto;
import io.hhplus.commerce.presentation.controller.product.dto.ProductResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
@Tag(name = "/api/member", description = "장바구니 관련 API")
public class CartController {
    private final CartService cartService;

    @Operation(summary = "장바구니에 상품 담기")
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(mediaType = "application/json"))
    @PostMapping("/{memberId}/cart")
    public void putIn(@RequestBody CartPutInDto dto) {
        cartService.putIn(dto);
    }

    @Operation(summary = "장바구니에서 상품 삭제")
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(mediaType = "application/json"))
    @DeleteMapping("/cart/{cartId}")
    public void putOff(@PathVariable (name = "cartId")Long cartId) {
        cartService.putOff(cartId);
    }

    @Operation(summary = "장바구니 목록 조회")
    @ApiResponse(responseCode = "200", description = "성공", content = {@Content(
            mediaType = "application/json",
            array = @ArraySchema(schema = @Schema(implementation = ProductResponseDto.class))
    )})
    @GetMapping("/{memberId}/cart")
    public List<ProductResponseDto> getCart(@PathVariable (name = "memberId")Long memberId) {
        return cartService.getProductsInCart(memberId);
    }

    @Operation(summary = "장바구니에 담긴 상품의 수량 변경")
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(mediaType = "application/json"))
    @PatchMapping("/{memberId}/cart/products")
    public void changeQuantity(@RequestBody ChangeQuantityDto dto) {
        cartService.changeQuantity(dto);
    }
}
