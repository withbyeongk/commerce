package io.hhplus.commerce.application.service;

import io.hhplus.commerce.application.service.member.CartService;
import io.hhplus.commerce.domain.member.Cart;
import io.hhplus.commerce.infra.repository.member.CartRepository;
import io.hhplus.commerce.presentation.controller.member.dto.ChangeQuantityDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartServiceUnitTest {

    @InjectMocks
    CartService cartService;

    @Mock
    private CartRepository cartRepository;

    @Test
    @DisplayName("장바구니 상품 수량 변경 성공")
    void changeQuantityTest() {
        // given
        Long cartId = 1L;
        Long memberId = 2L;
        Long productId = 3L;
        int beforeQuantity = 3;
        int afterQuantity = 5;
        ChangeQuantityDto dto = new ChangeQuantityDto(cartId, afterQuantity);

        Cart cart = new Cart(cartId, memberId, productId, beforeQuantity);

        // when
        when(cartRepository.findById(cartId)).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenAnswer(invocation -> invocation.getArgument(0));

        cartService.changeQuantity(dto);

        // then
        verify(cartRepository).findById(cartId);
        verify(cartRepository).save(cart);
        assertEquals(afterQuantity, cart.getQuantity());
    }

}