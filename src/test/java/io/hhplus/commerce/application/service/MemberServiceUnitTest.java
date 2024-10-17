package io.hhplus.commerce.application.service;

import io.hhplus.commerce.domain.entity.Member;
import io.hhplus.commerce.domain.entity.Point;
import io.hhplus.commerce.infra.repository.MemberRepository;
import io.hhplus.commerce.infra.repository.PointRepository;
import io.hhplus.commerce.presentation.dto.ChargePointDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MemberServiceUnitTest {

    @InjectMocks
    MemberService memberService;

    @Mock
    private MemberRepository memberRepository;
    @Mock
    private PointRepository pointRepository;

    @BeforeEach
    void setup() {

    }

    @Test
    @DisplayName("회원 포인트 충전 시 잘못된 회원 ID가 입력될 경우 에러 발생")
    void invalidMemberId() {
        // given
        Long memberId = 100L;
        ChargePointDto dto = new ChargePointDto(memberId, 1000);

        // when
        when(memberRepository.findById(memberId)).thenReturn(Optional.empty());

        // then
        assertThrows(IllegalArgumentException.class, () -> {
            memberService.chargePoint(dto);
        });
    }

    @Test
    @DisplayName("회원 포인트 충전 성공")
    void chargeTest() {
        // given
        Long memberId = 1L;
        int initPoint = 1000;
        int chargePoint = 2000;
        int chargedPoint = initPoint + chargePoint;
        Member member = new Member(memberId, "회원", initPoint, null, null, LocalDateTime.now());
        Point point = new Point(memberId, initPoint);
        ChargePointDto dto = new ChargePointDto(memberId, chargePoint);

        // when
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(pointRepository.findById(memberId)).thenReturn(Optional.of(point));

        // then
        assertEquals(memberService.chargePoint(dto), chargedPoint);

    }
}