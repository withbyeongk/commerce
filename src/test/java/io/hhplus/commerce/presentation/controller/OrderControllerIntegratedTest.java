package io.hhplus.commerce.presentation.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import io.hhplus.commerce.presentation.controller.order.dto.OrderResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderControllerIntegratedTest {

    @LocalServerPort
    private int port;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PointRepository pointRepository;

    @Autowired
    private ProductStockRepository productStockRepository;

    private RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private ObjectMapper objectMapper;

    private String baseUrl;

    @BeforeEach
    public void setup() {
        baseUrl = "http://localhost:" + port;
    }

    @Test
    @DisplayName("주문 테스트 성공")
    public void makeOrderTest() throws JsonProcessingException {
        // given
        Member member = new Member(null, "회원1", 10000, null, null, LocalDateTime.now());
        Member savedMember = memberRepository.save(member);
        Product product = new Product("상품1", 1000, 20, "상품 설명");
        Product savedProduct = productRepository.save(product);
        Point point = new Point(savedMember.getId(), 10000);
        Point savedPoint = pointRepository.save(point);
        ProductStock productStock = new ProductStock(savedProduct.getId(), 20);
        ProductStock savedStock = productStockRepository.save(productStock);

        List<OrderRequestDto.OrderItemRequestDto> products = Arrays.asList(
                new OrderRequestDto.OrderItemRequestDto(savedProduct.getId(), 1)
        );

        OrderRequestDto orderRequestDto = new OrderRequestDto(savedMember.getId(), products);

        // when
        ResponseEntity<OrderResponseDto> response = restTemplate.postForEntity(
                baseUrl + "/api/member/{memberId}/order",
                new HttpEntity<>(objectMapper.writeValueAsString(orderRequestDto), createJsonHeader()),
                OrderResponseDto.class,
                member.getId());

        // then
        Product resultProduct = productRepository.findById(savedProduct.getId()).get();
        Member resultMember = memberRepository.findById(savedMember.getId()).get();
        ProductStock resultStock = productStockRepository.findById(savedStock.getId()).get();

        assertEquals(19, resultStock.getStock());
        assertEquals(9000, resultMember.getPoint());

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        OrderResponseDto resultOrder = response.getBody();
        assertNotNull(resultOrder);
        assertEquals(1, resultOrder.orderId());
    }

    private HttpHeaders createJsonHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

}