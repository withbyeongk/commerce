package io.hhplus.commerce.domain.entity;

import io.hhplus.commerce.common.exception.CommerceErrorCodes;
import io.hhplus.commerce.common.exception.CommerceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProductTest {

    @Test
    @DisplayName("재고 차감 시 입력값이 음수이거나 0이면 에러가 발생합니다.")
    void invalidQuantityInStockMinus() {
        // given
        ProductStock stock = new ProductStock(1L, 100);

        // expected
        CommerceException e1 = assertThrows(CommerceException.class, () -> {
            stock.minus(-1);
        });
        CommerceException e2 = assertThrows(CommerceException.class, () -> {
            stock.minus(0);
        });

        // then
        assertEquals(CommerceErrorCodes.INVALID_ARGUMENTS_QUANTITY, e1.getErrorCode());
        assertEquals(CommerceErrorCodes.INVALID_ARGUMENTS_QUANTITY, e2.getErrorCode());
    }

    @Test
    @DisplayName("재고 차감 시 재고가 차감할 수보다 적은 경우에 에러가 발생합니다.")
    void insufficientInStockMinus() {
        // given
        ProductStock stock = new ProductStock(1L, 10);

        // expected
        CommerceException e = assertThrows(CommerceException.class, () -> {
            stock.minus(11);
        });

        // then
        assertEquals(CommerceErrorCodes.INSUFFICIENT_STOCK, e.getErrorCode());
    }

    @Test
    @DisplayName("재고 추가 시 입력값이 음수이거나 0이면 에러가 발생합니다.")
    void invalidQuantityInStockPlus() {
        // given
        ProductStock stock = new ProductStock(1L, 100);

        // expected
        CommerceException e = assertThrows(CommerceException.class, () -> {
            stock.plus(0);
        });

        // then
        assertEquals(CommerceErrorCodes.INVALID_ARGUMENTS_QUANTITY, e.getErrorCode());
    }
}