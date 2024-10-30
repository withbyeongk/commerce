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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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

    private Member createMember() {
        return new Member("회원1", 10000);
    }
    private Point createPoint(Long memberId) {
        return new Point(memberId, 10000);
    }

    private Product createProduct() {
        return new Product("상품1", 1000, 100, "상품1설명");
    }
    private ProductStock createProductStock(Long productId) {
        return new ProductStock(productId, 100);
    }

    @Test
    @DisplayName("주문 동시성 테스트 성공. 상품 하나씩 열번을 주문했을 때 재고 10개 소진 및 그에 해당하는 금액 사용")
    public void successfulOrderTest() throws Exception {

        Member savedMember = memberRepository.save(createMember());
        pointRepository.save(createPoint(savedMember.getId()));
        Product savedProduct = productRepository.save(createProduct());
        productStockRepository.save(createProductStock(savedProduct.getId()));


        ExecutorService executorService = Executors.newFixedThreadPool(10);

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

        for (int i = 0; i < 10; i++) {
            executorService.submit(task);
        }

        latch.countDown();
        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.SECONDS);


        Point resultPoint = pointRepository.findById(savedMember.getId()).get();
        Member resultMember = memberRepository.findById(savedMember.getId()).get();
        ProductStock resultStock = productStockRepository.findById(savedProduct.getId()).get();
        Product resultProduct = productRepository.findById(savedProduct.getId()).get();

        assertEquals(new Point(savedMember.getId(), 0), resultPoint);
        assertEquals(0, resultMember.getPoint());
        assertEquals(new ProductStock(savedProduct.getId(), 90), resultStock);
        assertEquals(90, resultProduct.getStock());
    }
}
