package io.hhplus.commerce.domain.entity;

import io.hhplus.commerce.common.exception.CommerceErrorCodes;
import io.hhplus.commerce.common.exception.CommerceException;
import io.hhplus.commerce.domain.member.Cart;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CartTest {

    @Test
    @DisplayName("카트 객체 생성 시 상품 개수의 기본값은 1개가 된다.")
    void defaultPoint() {
        // given
        Long cartId = 1L;

        // when
        Cart cart = new Cart(cartId, 1L);

        // then
        assertEquals(1, cart.getQuantity());
    }

    @Test
    @DisplayName("카트에 담긴 상품의 개수를 더할 때 양수가 아닌 값이 입력되면 에러가 발생합니다.")
    void invalidQuantityInPlus() {
        // given
        Long memberId = 1L;
        Long productId = 2L;
        Cart cart = new Cart(memberId, productId);

        // expected
        CommerceException e1 = assertThrows(CommerceException.class, () -> {
            cart.plus(0);
        });
        CommerceException e2 = assertThrows(CommerceException.class, () -> {
            cart.plus(0);
        });

        // then
        assertEquals(CommerceErrorCodes.INVALID_ARGUMENTS_QUANTITY, e1.getErrorCode());
        assertEquals(CommerceErrorCodes.INVALID_ARGUMENTS_QUANTITY, e2.getErrorCode());
    }

    @Test
    @DisplayName("카트에 담긴 상품의 개수를 뺄 때 양수가 아닌 값이 입력되면 에러가 발생합니다.")
    void invalidQuantityInMinus() {
        // given
        Long memberId = 1L;
        Long productId = 2L;
        int beforeAount = 5;
        Cart cart = new Cart(memberId, productId, beforeAount);

        // expected
        CommerceException e1 = assertThrows(CommerceException.class, () -> {
            cart.minus(-1);
        });
        CommerceException e2 = assertThrows(CommerceException.class, () -> {
            cart.minus(0);
        });

        // then
        assertEquals(CommerceErrorCodes.INVALID_ARGUMENTS_QUANTITY, e1.getErrorCode());
        assertEquals(CommerceErrorCodes.INVALID_ARGUMENTS_QUANTITY, e2.getErrorCode());
    }

    @Test
    @DisplayName("카트에 담긴 상품의 개수를 뺄 때 현재 담겨있는 개수보다 크거나 같은 값이 입력될 경우 에러가 발생한다.")
    void invalidQuantityInMinus2() {
        // given
        Long memberId = 1L;
        Long productId = 2L;
        int beforeAount = 5;
        Cart cart = new Cart(memberId, productId, beforeAount);

        // expected
        CommerceException e1 = assertThrows(CommerceException.class, () -> {
            cart.minus(5);
        });
        CommerceException e2 = assertThrows(CommerceException.class, () -> {
            cart.minus(6);
        });

        // then
        assertEquals(CommerceErrorCodes.INVALID_EXCEED_QUANTITY, e1.getErrorCode());
        assertEquals(CommerceErrorCodes.INVALID_EXCEED_QUANTITY, e2.getErrorCode());
    }

    @Test
    @DisplayName("카트에 담긴 상품의 개수를 변경할 때 양수가 아닌 값이 입력되면 에러가 발생합니다.")
    void invalidQuantityInChangeQuantity() {
        // given
        Long memberId = 1L;
        Long productId = 2L;
        Cart cart = new Cart(memberId, productId);

        // expected
        CommerceException e1 = assertThrows(CommerceException.class, () -> {
            cart.changeQuantity(-1);
        });
        CommerceException e2 = assertThrows(CommerceException.class, () -> {
            cart.changeQuantity(0);
        });

        // then
        assertEquals(CommerceErrorCodes.INVALID_ARGUMENTS_QUANTITY, e1.getErrorCode());
        assertEquals(CommerceErrorCodes.INVALID_ARGUMENTS_QUANTITY, e2.getErrorCode());
    }

}