package io.hhplus.commerce.presentation.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.hhplus.commerce.application.service.ProductService;
import io.hhplus.commerce.domain.entity.Product;
import io.hhplus.commerce.infra.repository.ProductRepository;
import io.hhplus.commerce.presentation.controller.product.dto.ProductRequestDto;
import io.hhplus.commerce.presentation.controller.product.dto.ProductResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProductControllerIntegratedTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @LocalServerPort
    private int port;

    String baseUrl;


    @BeforeEach
    public void setup() {
        baseUrl = "http://localhost:" + port;
    }

    @Test
    @DisplayName("상품 추가 성공")
    public void addProductTest() {
        // given
        ProductRequestDto dto = new ProductRequestDto("product1", 1000, 20, "product1 description");

        HttpEntity<ProductRequestDto> request = new HttpEntity<>(dto);

        // when
        ResponseEntity<Long> responseEntity = restTemplate.postForEntity(baseUrl + "/api/products/add", request, Long.class);

        // then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }

    @Test
    @DisplayName("상품 목록 조회")
    public void findAllProductsTest() throws IOException {
        // given
        for (int i = 1; i <= 25; i++) {
            Product product = new Product("상품" + i, 100 * i, 20+i, "상품" + i +" 설명");
            productRepository.save(product);
        }

        // when
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                baseUrl + "/api/products/all" + "?page=0&size=20",
                HttpMethod.GET,
                null,
                String.class);

        objectMapper.registerModule(new JavaTimeModule());
        JsonNode root = objectMapper.readTree(responseEntity.getBody());
        JsonNode content = root.path("content");
        List<ProductResponseDto> products = objectMapper.readValue(content.toString(), new TypeReference<List<ProductResponseDto>>(){});

        // then
        assertNotNull(products);
        assertEquals(20, products.size());
    }

    @Test
    @DisplayName("베스트셀러 상품 목록 조회")
    public void findBestsellersTest() {
        // when
        List<ProductResponseDto> products = Arrays.asList(restTemplate.getForObject(baseUrl+"/api/products/bestsellers", ProductResponseDto[].class));

        // then
        assertNotNull(products);
    }

    @Test
    @DisplayName("상품 상세 조회 성공")
    public void findByIdTest() {
        // given
        Product product = new Product("상품1", 100, 20, "상품 설명");
        productRepository.save(product);

        Long productId = product.getId();

        // When
        ProductResponseDto responseDto = restTemplate.getForObject(baseUrl+"/api/products/"+productId, ProductResponseDto.class);

        // Then
        assertNotNull(responseDto);
        assertEquals(product.getId(), responseDto.productId());
        assertEquals(product.getName(), responseDto.name());
        assertEquals(product.getPrice(), responseDto.price());
        assertEquals(product.getStock(), responseDto.stock());
    }
}