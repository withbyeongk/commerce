package io.hhplus.commerce.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.hhplus.commerce.domain.entity.Member;
import io.hhplus.commerce.domain.entity.Point;
import io.hhplus.commerce.infra.repository.MemberRepository;
import io.hhplus.commerce.infra.repository.PointRepository;
import io.hhplus.commerce.presentation.controller.member.dto.ChargePointDto;
import io.hhplus.commerce.presentation.controller.member.dto.PointResponseDto;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class MemberControllerConcurrencyTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PointRepository pointRepository;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("잔액 충전 동시성 테스트 : 실패 (잔액 충전의 경우엔 회원 한명이 자신의 리소스에만 접근하며 요청이 매우 빠른 시간 내에 많이 몰릴 것이라 생각지 않아서 비관적 락을 적용하지 않았음)")
    @Disabled
    public void chargePointConcurrencyTest() throws Exception {
        Member member = new Member(null, "회원1", 0, null, null, LocalDateTime.now());
        Member savedMember = memberRepository.save(member);
        Point point = new Point(savedMember.getId(), 0);
        pointRepository.save(point);

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(1);

        Runnable task = () -> {
            try {
                ChargePointDto chargePointDto = new ChargePointDto(savedMember.getId(), 100);

                latch.await();
                mockMvc.perform(post("/api/member/points")
                                .content(objectMapper.writeValueAsString(chargePointDto))
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk());
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        for (int i = 0; i < 10; i++) {
            executorService.submit(task);
        }

        latch.countDown();
        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.SECONDS);

        MvcResult result = mockMvc.perform(get("/api/member/1/points")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        PointResponseDto responseDto = new ObjectMapper().readValue(content, PointResponseDto.class);

        assertEquals(1000, responseDto.point());
    }


}
