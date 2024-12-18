package io.hhplus.commerce.presentation.controller;

import io.hhplus.commerce.domain.member.Cart;
import io.hhplus.commerce.domain.member.Member;
import io.hhplus.commerce.domain.product.Product;
import io.hhplus.commerce.infra.repository.member.CartRepository;
import io.hhplus.commerce.infra.repository.member.MemberRepository;
import io.hhplus.commerce.infra.repository.product.ProductRepository;
import io.hhplus.commerce.presentation.controller.member.dto.CartPutInDto;
import io.hhplus.commerce.presentation.controller.member.dto.ChangeQuantityDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

import static io.hhplus.commerce.common.DummyFactory.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CartControllerIntegratedTest {

    @LocalServerPort
    private int port;

    private String baseUrl;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProductRepository productRepository;

    private RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private TestRestTemplate testRestTemplate;

    @BeforeEach
    public void setup() {
        baseUrl = "http://localhost:" + port;
    }

    @Test
    @DisplayName("카트에 상품 담기")
    public void putInCartTest() throws Exception {

        Member savedMember = memberRepository.save(createMember());
        Product savedProduct = productRepository.save(createProduct());

        CartPutInDto cartPutInDto = new CartPutInDto(savedMember.getId(), savedProduct.getId());

        ResponseEntity<Void> responseEntity =
                restTemplate.postForEntity(
                        baseUrl + "/api/member/{memberId}/cart",
                        cartPutInDto,
                        Void.class,
                        Collections.singletonMap("memberId", 1));

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("카트에서 상품 삭제하기")
    public void putOffCartTest() throws Exception {

        Member savedMember = memberRepository.save(createMember());
        Product savedProduct = productRepository.save(createProduct());

        Cart cart = new Cart(null, savedMember.getId(), savedProduct.getId(), 1);
        Cart savedCart = cartRepository.save(cart);

        ResponseEntity<Void> responseEntity =
                restTemplate.exchange(
                baseUrl + "/api/member/cart/{cartId}",
                HttpMethod.DELETE,
                null,
                Void.class,
                Collections.singletonMap("cartId", savedCart.getId())
        );

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void shouldGetCart() throws Exception {
        // given
        Member savedMember = memberRepository.save(createMember());
        Product savedProduct = productRepository.save(createProduct());
        cartRepository.save(createCart(savedMember.getId(), savedProduct.getId()));

        // when
        ResponseEntity<List> responseEntity =
                restTemplate.exchange(
                "http://localhost:" + port + "/api/member/{memberId}/cart",
                HttpMethod.GET,
                null,
                List.class,
                Collections.singletonMap("memberId", savedMember.getId())
        );

        // then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    @Disabled
    public void shouldChangeQuantity() throws Exception {
        // given
        Member savedMember = memberRepository.save(createMember());
        Product savedProduct = productRepository.save(createProduct());
        Cart savedCart = cartRepository.save(createCart(savedMember.getId(), savedProduct.getId()));

        ChangeQuantityDto dto = new ChangeQuantityDto(savedCart.getId(),5);

        HttpEntity<ChangeQuantityDto> requestUpdate = new HttpEntity<>(dto);

        ResponseEntity<Void> responseEntity =
                testRestTemplate.exchange(
                baseUrl + "/api/member/{memberId}/cart/products",
                HttpMethod.PATCH,
                requestUpdate,
                Void.class,
                Collections.singletonMap("memberId", savedMember.getId())
        );

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        Cart updatedCart = cartRepository.findById(savedCart.getId()).orElse(null);
        assertEquals(dto.quantity(), updatedCart.getQuantity());
    }
}