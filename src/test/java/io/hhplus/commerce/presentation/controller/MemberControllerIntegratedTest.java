package io.hhplus.commerce.presentation.controller;

import io.hhplus.commerce.domain.entity.Member;
import io.hhplus.commerce.domain.entity.Point;
import io.hhplus.commerce.infra.repository.MemberRepository;
import io.hhplus.commerce.infra.repository.PointRepository;
import io.hhplus.commerce.presentation.dto.ChargePointDto;
import io.hhplus.commerce.presentation.dto.PointResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MemberControllerIntegratedTest {

    @LocalServerPort
    private int port;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PointRepository pointRepository;

    private RestTemplate restTemplate = new RestTemplate();

    private String baseUrl;

    @BeforeEach
    public void setup() {
        baseUrl = "http://localhost:" + port;
    }

    @Test
    public void shouldChargePoint() {
        // given
        int beforePoint = 10000;
        Member member = new Member(null, "회원1", beforePoint, null, null, LocalDateTime.now());
        Member savedMember = memberRepository.save(member);
        Point point = new Point(savedMember.getId(), 50);
        Point savedPoint = pointRepository.save(point);

        ChargePointDto dto = new ChargePointDto(savedMember.getId(), 50); // 임시 값

        HttpEntity<ChargePointDto> request = new HttpEntity<>(dto);

        ResponseEntity<Void> responseEntity =
                restTemplate.exchange(
                baseUrl + "/api/member/points",
                HttpMethod.POST,
                request,
                Void.class
        );

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        Member updatedMember = memberRepository.findById(savedMember.getId()).orElse(null);
        assertEquals(dto.points() + beforePoint, updatedMember.getPoint());
    }

    @Test
    public void shouldGetPoint() {
        // given
        Member member = new Member(null, "회원1", 10000, null, null, LocalDateTime.now());
        Member savedMember = memberRepository.save(member);
        Point point = new Point(savedMember.getId(), 50);
        Point savedPoint = pointRepository.save(point);

        // 요청 실행
        ResponseEntity<PointResponseDto> responseEntity =
                restTemplate.exchange(
                baseUrl + "/api/member/" + savedMember.getId() + "/points",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<PointResponseDto>() {}
        );

        // 응답 상태 코드 검증
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        // 응답 본문 검증
        PointResponseDto dto = responseEntity.getBody();
        assertNotNull(dto);
        assertEquals(savedMember.getPoint(), dto.point());
    }

}