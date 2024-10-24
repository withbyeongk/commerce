package io.hhplus.commerce.application.service;

import io.hhplus.commerce.common.exception.CommerceErrorCodes;
import io.hhplus.commerce.common.exception.CommerceException;
import io.hhplus.commerce.domain.entity.Cart;
import io.hhplus.commerce.domain.entity.Product;
import io.hhplus.commerce.infra.repository.CartRepository;
import io.hhplus.commerce.infra.repository.MemberRepository;
import io.hhplus.commerce.infra.repository.ProductRepository;
import io.hhplus.commerce.presentation.controller.cart.dto.CartPutInDto;
import io.hhplus.commerce.presentation.controller.cart.dto.ChangeQuantityDto;
import io.hhplus.commerce.presentation.controller.product.dto.ProductResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    public Long putIn(CartPutInDto dto) {

        memberRepository.findById(dto.memberId()).orElseThrow(() -> new CommerceException(CommerceErrorCodes.MEMBER_NOT_FOUND));
        productRepository.findById(dto.productId()).orElseThrow(() -> new CommerceException(CommerceErrorCodes.PRODUCT_NOT_FOUND));

        Cart cart = new Cart(dto.memberId(), dto.productId());
        cartRepository.save(cart);
        return cart.getId();
    }

    public void putOff(Long cartId) {
        cartRepository.deleteById(cartId);
    }

    public List<ProductResponseDto> getProductsInCart(Long memberId) {
        List<ProductResponseDto> response = new ArrayList<>();

        List<Cart> carts = cartRepository.findByMemberId(memberId);

        if (carts.isEmpty()) {
            return response;
        }

        // 카트에 있는 상품 정보 조회
        for (Cart cart : carts) {
            Long productId = cart.getProductId();

            Product product = productRepository.findById(productId).get();

            response.add(product.toResponseDto());
        }

        return response;

    }

    public void changeQuantity(ChangeQuantityDto dto) {
        Cart cart = cartRepository.findById(dto.cartId()).orElseThrow(() -> new CommerceException(CommerceErrorCodes.CART_NOT_FOUND));

        int beforeQuantity = cart.getQuantity();

        if (beforeQuantity == dto.quantity()) {
            return ;
        } else if (beforeQuantity == 0) {
            cartRepository.deleteById(cart.getId());
            return ;
        }
        cart.changeQuantity(dto.quantity());
        cartRepository.save(cart);
    }
}
