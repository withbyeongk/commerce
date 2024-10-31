package io.hhplus.commerce.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.hhplus.commerce.domain.entity.Member;
import io.hhplus.commerce.infra.repository.MemberRepository;
import io.hhplus.commerce.infra.repository.PointRepository;
import io.hhplus.commerce.presentation.controller.member.dto.ChargePointDto;
import io.hhplus.commerce.presentation.controller.member.dto.PointResponseDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static io.hhplus.commerce.common.DummyFactory.createMember;
import static io.hhplus.commerce.common.DummyFactory.createPoint;
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

    @Autowired
    private ObjectMapper objectMapper;

    private long startTime;
    private long endTime;
    @BeforeEach
    void setUp() {
        startTime = System.nanoTime();
    }

    @AfterEach
    void tearDown() {
        endTime = System.nanoTime();
        System.out.println("taken time : " + (endTime - startTime));
    }

    @Test
    @DisplayName("잔액 충전 동시성 테스트 성공.")
    public void chargePointConcurrencyTest() throws Exception {
        Member savedMember = memberRepository.save(createMember());
        pointRepository.save(createPoint(savedMember.getId()));
        int repeatCount = 10;
        int chargePoint = 100;

        ExecutorService executorService = Executors.newFixedThreadPool(repeatCount);
        CountDownLatch latch = new CountDownLatch(1);

        Runnable task = () -> {
            try {
                ChargePointDto chargePointDto = new ChargePointDto(savedMember.getId(), chargePoint);

                latch.await();
                mockMvc.perform(post("/api/member/points")
                                .content(objectMapper.writeValueAsString(chargePointDto))
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk());
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        for (int i = 0; i < repeatCount; i++) {
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
        PointResponseDto responseDto = objectMapper.readValue(content, PointResponseDto.class);

        assertEquals(10000 + chargePoint * repeatCount, responseDto.point());
    }


}
