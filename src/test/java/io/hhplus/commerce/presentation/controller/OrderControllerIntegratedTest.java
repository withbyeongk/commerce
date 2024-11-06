package io.hhplus.commerce.presentation.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.hhplus.commerce.domain.member.Member;
import io.hhplus.commerce.domain.product.Product;
import io.hhplus.commerce.domain.product.ProductStock;
import io.hhplus.commerce.infra.repository.member.MemberRepository;
import io.hhplus.commerce.infra.repository.member.PointRepository;
import io.hhplus.commerce.infra.repository.product.ProductRepository;
import io.hhplus.commerce.infra.repository.product.ProductStockRepository;
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

import java.util.Arrays;
import java.util.List;

import static io.hhplus.commerce.common.DummyFactory.*;
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
        // given;
        Member savedMember = memberRepository.save(createMember());
        pointRepository.save(createPoint(savedMember.getId()));
        Product savedProduct = productRepository.save(createProduct());
        ProductStock savedStock = productStockRepository.save(createProductStock(savedProduct.getId()));

        List<OrderRequestDto.OrderItemRequestDto> products = Arrays.asList(
                new OrderRequestDto.OrderItemRequestDto(savedProduct.getId(), 1)
        );

        OrderRequestDto orderRequestDto = new OrderRequestDto(savedMember.getId(), products);

        // when
        ResponseEntity<OrderResponseDto> response = restTemplate.postForEntity(
                baseUrl + "/api/member/{memberId}/order",
                new HttpEntity<>(objectMapper.writeValueAsString(orderRequestDto), createJsonHeader()),
                OrderResponseDto.class,
                savedMember.getId());

        // then
        Product resultProduct = productRepository.findById(savedProduct.getId()).get();
        Member resultMember = memberRepository.findById(savedMember.getId()).get();
        ProductStock resultStock = productStockRepository.findById(savedStock.getId()).get();

        assertNotNull(response);
        assertEquals(99, resultStock.getStock());
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