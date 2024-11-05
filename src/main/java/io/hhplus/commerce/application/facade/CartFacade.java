package io.hhplus.commerce.application.facade;

import io.hhplus.commerce.application.facade.usecase.CartUsecase;
import io.hhplus.commerce.application.service.member.CartService;
import io.hhplus.commerce.application.service.member.MemberService;
import io.hhplus.commerce.application.service.product.ProductService;
import io.hhplus.commerce.common.exception.CommerceErrorCodes;
import io.hhplus.commerce.common.exception.CommerceException;
import io.hhplus.commerce.domain.member.Cart;
import io.hhplus.commerce.domain.product.Product;
import io.hhplus.commerce.presentation.controller.member.dto.CartListDto;
import io.hhplus.commerce.presentation.controller.member.dto.CartPutInDto;
import io.hhplus.commerce.presentation.controller.member.dto.ChangeQuantityDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartFacade implements CartUsecase {
    private final CartService cartService;
    private final ProductService productService;
    private final MemberService memberService;

    @Override
    public void putIn(CartPutInDto dto) {
        validateMember(dto.memberId());
        validateProduct(dto.productId());

        cartService.putIn(dto);
    }

    @Override
    public void putOff(Long cartId) {
        cartService.putOff(cartId);
    }

    @Override
    public void changeQuantity(ChangeQuantityDto dto) {
        cartService.changeQuantity(dto);
    }


    @Override
    public List<CartListDto> getProductsInCart(Long memberId) {
        validateMember(memberId);

        List<Cart> carts = cartService.findByMemberId(memberId);
        if (carts.isEmpty()) {
            return List.of();
        }

        List<Long> productIds = getProductIds(carts);
        List<Product> products = productService.findAllByIds(productIds);

        return makeProductResponseDto(carts, products);
    }

    private void validateMember(Long memberId) {
        memberService.lookupMember(memberId);
    }

    private void validateProduct(Long productId) {
        Product product = productService.getProduct(productId);
        if (product.getStock() == 0) {
            throw new CommerceException(CommerceErrorCodes.INSUFFICIENT_STOCK);
        }
    }

    private List<Long> getProductIds(List<Cart> carts) {
        return carts.stream()
                .map(Cart::getProductId)
                .toList();
    }

    private List<CartListDto> makeProductResponseDto(List<Cart> carts, List<Product> products) {
        Map<Long, Integer> cartMap = carts.stream()
                .collect(Collectors.toMap(Cart::getProductId, Cart::getQuantity));

        List<CartListDto> cartListDtos = products.stream().map(product -> {
            int quantity = cartMap.getOrDefault(product.getId(), 0);

            return new CartListDto(product, quantity);
        }).collect(Collectors.toList());

        return cartListDtos;
    }

}
