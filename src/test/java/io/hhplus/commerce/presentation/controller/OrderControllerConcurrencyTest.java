package io.hhplus.commerce.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.hhplus.commerce.domain.entity.Member;
import io.hhplus.commerce.domain.entity.Point;
import io.hhplus.commerce.domain.entity.Product;
import io.hhplus.commerce.domain.entity.ProductStock;
import io.hhplus.commerce.infra.repository.MemberRepository;
import io.hhplus.commerce.infra.repository.PointRepository;
import io.hhplus.commerce.infra.repository.ProductRepository;
import io.hhplus.commerce.infra.repository.ProductStockRepository;
import io.hhplus.commerce.presentation.controller.order.dto.OrderRequestDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static io.hhplus.commerce.common.DummyFactory.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerConcurrencyTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    private PointRepository pointRepository;

    @Autowired
    ProductStockRepository productStockRepository;

    private long start;
    private long end;

    @BeforeEach
    public void startTime() {
        start = System.nanoTime();
    }

    @AfterEach
    public void endTime() {
        end = System.nanoTime();
        System.out.println(end - start);
    }

    @Test
    @DisplayName("주문 동시성 테스트 성공. 상품 하나씩 열번을 주문했을 때 재고 10개 소진 및 그에 해당하는 금액 사용")
    public void successfulOrderTest() throws Exception {

        Member savedMember = memberRepository.save(createMember());
        pointRepository.save(createPoint(savedMember.getId()));
        Product savedProduct = productRepository.save(createProduct());
        productStockRepository.save(createProductStock(savedProduct.getId()));

        int repeatCount = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(repeatCount);

        List<OrderRequestDto.OrderItemRequestDto> items = new ArrayList<>();
        items.add(new OrderRequestDto.OrderItemRequestDto(savedProduct.getId(), 1));
        OrderRequestDto orderDto = new OrderRequestDto(savedMember.getId(), items);

        CountDownLatch latch = new CountDownLatch(1);
        Runnable task = () -> {
            try {
                latch.await();
                // Act
                mockMvc.perform(post("/api/member/" + savedMember.getId() + "/order")
                                .content(objectMapper.writeValueAsString(orderDto))
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


        Point resultPoint = pointRepository.findById(savedMember.getId()).get();
        Member resultMember = memberRepository.findById(savedMember.getId()).get();
        ProductStock resultStock = productStockRepository.findById(savedProduct.getId()).get();
        Product resultProduct = productRepository.findById(savedProduct.getId()).get();

        assertEquals(new Point(savedMember.getId(), 10000 - repeatCount * 100), resultPoint);
        assertEquals(10000 - repeatCount * 100, resultMember.getPoint());
        assertEquals(new ProductStock(savedProduct.getId(), 100 - repeatCount), resultStock);
        assertEquals(100 - repeatCount, resultProduct.getStock());
    }
}
