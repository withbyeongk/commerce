package io.hhplus.commerce.application.service.member;

import io.hhplus.commerce.common.exception.CommerceErrorCodes;
import io.hhplus.commerce.common.exception.CommerceException;
import io.hhplus.commerce.domain.member.Cart;
import io.hhplus.commerce.infra.repository.member.CartRepository;
import io.hhplus.commerce.presentation.controller.member.dto.CartPutInDto;
import io.hhplus.commerce.presentation.controller.member.dto.ChangeQuantityDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;

    public Long putIn(CartPutInDto dto) {
        return cartRepository.save(new Cart(dto.memberId(), dto.productId()))
                .getId();
    }

    public void putOff(Long cartId) {
        cartRepository.findById(cartId).orElseThrow(() -> new CommerceException(CommerceErrorCodes.CART_NOT_FOUND));
        cartRepository.deleteById(cartId);
    }

    public List<Cart> findByMemberId(Long memberId) {
        return cartRepository.findByMemberId(memberId);
    }

    public void changeQuantity(ChangeQuantityDto dto) {
        Cart cart = cartRepository.findById(dto.cartId()).orElseThrow(() -> new CommerceException(CommerceErrorCodes.CART_NOT_FOUND));

        int beforeQuantity = cart.getQuantity();

        if (beforeQuantity == dto.quantity()) {
            return ;
        } else if (beforeQuantity == 0) {
            cartRepository.deleteById(cart.getId());
            log.info("상품의 수를 0으로 변경하는 것은 장바구니에서 상품 삭제처리됩니다.");
            return ;
        }
        cart.changeQuantity(dto.quantity());
        cartRepository.save(cart);
    }
}
