package io.hhplus.commerce.domain.entity;

import io.hhplus.commerce.common.exception.CommerceErrorCodes;
import io.hhplus.commerce.common.exception.CommerceException;
import io.hhplus.commerce.domain.member.Point;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PointTest {

    @Test
    @DisplayName("포인트 입력이 없을 경우 0포인트로 생성된다.")
    void defaultPointValueSettingCheck() {
        // given
        Long memberId = 1L;

        // when
        Point point = new Point(memberId);

        // then
        assertEquals(0, point.getPoint());
    }

    @Test
    @DisplayName("충전할 포인트가 음수이거나 0이면 에러가 발생합니다.")
    void invalidArgumentPointCheckInCharge() {
        // given
        Point point = new Point(1L, 100);

        // when & expected
        CommerceException e1 = assertThrows(CommerceException.class, () -> {
            point.charge(-1);
        });
        CommerceException e2 = assertThrows(CommerceException.class, () -> {
            point.charge(0);
        });

        // then
        assertEquals(CommerceErrorCodes.INVALID_ARGUMENTS_POINT, e1.getErrorCode());
        assertEquals(CommerceErrorCodes.INVALID_ARGUMENTS_POINT, e2.getErrorCode());
    }
    @Test
    @DisplayName("사용할 포인트가 음수이거나 0이면 에러가 발생합니다.")
    void invalidArgumentPointCheckInUse() {
        // given
        Point point = new Point(1L, 100);

        // when & expected
        CommerceException e1 = assertThrows(CommerceException.class, () -> {
            point.use(-1);
        });
        CommerceException e2 = assertThrows(CommerceException.class, () -> {
            point.use(0);
        });

        // then
        assertEquals(CommerceErrorCodes.INVALID_ARGUMENTS_POINT, e1.getErrorCode());
        assertEquals(CommerceErrorCodes.INVALID_ARGUMENTS_POINT, e2.getErrorCode());
    }


    @Test
    @DisplayName("포인트가 부족하여 사용할 수 없는 경우 에러 발생.")
    void insufficientPointErrorCheck() {
        // given
        Point point = new Point(1L, 100);

        // when & expected

        CommerceException e = assertThrows(CommerceException.class, () -> {
            point.use(101);
        });

        // then
        assertEquals(CommerceErrorCodes.INSUFFICIENT_POINT, e.getErrorCode());
    }

    @Test
    @DisplayName("포인트 충전 성공.")
    void chargePoint() {
        // given
        Point point = new Point(1L, 100);

        // when
        Point chargedPoint = point.charge(200);

        // then
        assertEquals(chargedPoint, point);
    }


    @Test
    @DisplayName("포인트 사용 성공.")
    void usePoint() {
        // given
        Point point = new Point(1L, 100);

        // when
        Point usedPoint = point.use(100);

        // then
        assertEquals(usedPoint, point);
    }
}