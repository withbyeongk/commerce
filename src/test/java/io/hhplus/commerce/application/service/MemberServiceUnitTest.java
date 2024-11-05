package io.hhplus.commerce.application.service;

import io.hhplus.commerce.application.service.member.MemberService;
import io.hhplus.commerce.common.exception.CommerceErrorCodes;
import io.hhplus.commerce.common.exception.CommerceException;
import io.hhplus.commerce.domain.member.Member;
import io.hhplus.commerce.domain.member.Point;
import io.hhplus.commerce.infra.repository.member.MemberRepository;
import io.hhplus.commerce.infra.repository.member.PointRepository;
import io.hhplus.commerce.presentation.controller.member.dto.ChargePointDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MemberServiceUnitTest {

    @InjectMocks
    MemberService memberService;

    @Mock
    private MemberRepository memberRepository;
    @Mock
    private PointRepository pointRepository;

    @Test
    @DisplayName("회원 포인트 충전 성공")
    void chargePointTest() {
        // given
        Long memberId = 1L;
        int initPoint = 1000;
        int addPoint = 2000;
        Point point = new Point(memberId, initPoint);

        ChargePointDto dto = new ChargePointDto(memberId, addPoint);
        Point chargePoint = point.charge(dto.points());
        Point expectedPoint = new Point(memberId, initPoint + addPoint);

        when(pointRepository.findById(memberId)).thenReturn(Optional.of(point));
        when(pointRepository.save(chargePoint)).thenReturn(expectedPoint);

        // when
        Point resultPoint = memberService.chargePoint(dto);

        // then
        verify(pointRepository).findById(memberId);
        assertEquals(expectedPoint, resultPoint);

    }

    @Test
    @DisplayName("회원 포인트 업데이트")
    void updatePointTest() {
        // given
        Long memberId = 1L;
        int initPoint = 1000;
        int updatePoint = 2000;
        Member member = new Member(memberId, "회원", initPoint);
        Member expectedMember = new Member(memberId, "회원", updatePoint);

        // Configure repository mock
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(memberRepository.save(member)).thenReturn(expectedMember);

        // when
        Member updatedResult = memberService.updatePoint(memberId, updatePoint);

        // then
        verify(memberRepository).findById(memberId);
        assertEquals(expectedMember, updatedResult);
    }

}