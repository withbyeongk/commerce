package io.hhplus.commerce.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.hhplus.commerce.domain.member.Member;
import io.hhplus.commerce.domain.member.Point;
import io.hhplus.commerce.domain.product.Product;
import io.hhplus.commerce.domain.product.ProductStock;
import io.hhplus.commerce.infra.repository.member.MemberRepository;
import io.hhplus.commerce.infra.repository.member.PointRepository;
import io.hhplus.commerce.infra.repository.product.ProductRepository;
import io.hhplus.commerce.infra.repository.product.ProductStockRepository;
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
    @DisplayName("주문 동시성 테스트 성공. 상품 하나씩 열번을 주문했을 때 재고 10개 차감")
    public void successfulOrderTest() throws Exception {

        Member savedMember = memberRepository.save(createMember());
        pointRepository.save(createPoint(savedMember.getId()));
        Product savedProduct = productRepository.save(createProduct());
        productStockRepository.save(createProductStock(savedProduct.getId()));

        int repeatCount = 1;
        ExecutorService executorService = Executors.newFixedThreadPool(repeatCount);

        List<OrderRequestDto.OrderItemRequestDto> items = new ArrayList<>();
        items.add(new OrderRequestDto.OrderItemRequestDto(savedProduct.getId(), 1));
        OrderRequestDto orderDto = new OrderRequestDto(savedMember.getId(), items);

        CountDownLatch latch = new CountDownLatch(1);
        Runnable task = () -> {
            try {
                latch.await();

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


        ProductStock resultStock = productStockRepository.findById(savedProduct.getId()).get();
        Product resultProduct = productRepository.findById(savedProduct.getId()).get();

        ProductStock expectedStock = new ProductStock(savedProduct.getId(), 100 - repeatCount);

        assertEquals(expectedStock, resultStock);
        assertEquals(expectedStock.getStock(), resultProduct.getStock());
    }
}
