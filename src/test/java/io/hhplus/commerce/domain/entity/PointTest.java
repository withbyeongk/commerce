package io.hhplus.commerce.domain.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PointTest {

    @Test
    @DisplayName("포인트 입력이 없을 경우 0포인트로 생성된다.")
    void defaultPoint() {
        // given
        Long memberId = 1L;

        // when
        Point point = new Point(memberId);

        // then
        assertEquals(0, point.getPoint());
    }

    @Test
    @DisplayName("충전할 포인트가 음수이거나 0이면 에러가 발생합니다.")
    void invalidPointValue() {
        // given
        Point point = new Point(1L, 100);

        // then
        assertThrows(IllegalArgumentException.class, () -> {
            point.charge(-1);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            point.charge(0);
        });
    }
}