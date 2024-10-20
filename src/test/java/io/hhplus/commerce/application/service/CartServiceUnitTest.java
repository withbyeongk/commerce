package io.hhplus.commerce.application.service;

import io.hhplus.commerce.domain.entity.Cart;
import io.hhplus.commerce.domain.entity.Member;
import io.hhplus.commerce.domain.entity.Product;
import io.hhplus.commerce.infra.repository.CartRepository;
import io.hhplus.commerce.infra.repository.MemberRepository;
import io.hhplus.commerce.infra.repository.ProductRepository;
import io.hhplus.commerce.presentation.dto.CartPutInDto;
import io.hhplus.commerce.presentation.dto.ChangeQuantityDto;
import io.hhplus.commerce.presentation.dto.ProductResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartServiceUnitTest {

    @InjectMocks
    CartService cartService;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private MemberRepository memberRepository;
    @Mock
    private ProductRepository productRepository;

    @BeforeEach
    void setup() {

    }

    @Test
    @DisplayName("장바구니에 상품 추가 성공")
    void putInTest() {
        // given
        Long memberId = 1L;
        Long productId = 2L;
        CartPutInDto dto = new CartPutInDto(memberId, productId);

        Member member = new Member();
        Product product = new Product();
        Cart cart = new Cart(memberId, productId);

        // when
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(cartRepository.save(any(Cart.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Long resultCartId = cartService.putIn(dto);

        // then
        assertEquals(cart.getId(), resultCartId);
    }


    @Test
    @DisplayName("회원 포인트 조회 시 잘못된 회원 ID가 입력될 경우 에러 발생")
    void invalidIdInPunIn() {
        // given
        CartPutInDto dto = new CartPutInDto(1L, 1L);

        // when
        when(memberRepository.findById(dto.memberId())).thenReturn(Optional.empty());

        // then
        assertThrows(IllegalArgumentException.class, () -> {
            cartService.putIn(dto);
        });
    }


    @Test
    @DisplayName("회원 포인트 조회 시 잘못된 상품 ID가 입력될 경우 에러 발생")
    void invalidProductIdInPunIn() {
        // given
        Member member = new Member(1L, "홍길동", 1000, null, null, LocalDateTime.now());
        CartPutInDto dto = new CartPutInDto(1L, 1L);

        // when
        when(memberRepository.findById(dto.memberId())).thenReturn(Optional.of(member));
        when(productRepository.findById(dto.productId())).thenReturn(Optional.empty());

        // then
        assertThrows(IllegalArgumentException.class, () -> {
            cartService.putIn(dto);
        });
    }

    @Test
    @DisplayName("장바구니에 담긴 상품 조회 성공")
    void testGetProductsInCart() {
        // given
        Long memberId = 1L;

        List<Cart> testCarts = new ArrayList<>();
        Cart cart1 = new Cart(memberId, 101L);
        Cart cart2 = new Cart(memberId, 102L);
        testCarts.add(cart1);
        testCarts.add(cart2);

        Product product1 = new Product(101L, "상품1", 1000, 100, "상품설명1", null, null, LocalDateTime.now());
        Product product2 = new Product(102L, "상품2", 2000, 50, "상품설명2", null, null, LocalDateTime.now());

        when(cartRepository.findByMemberId(memberId)).thenReturn(testCarts);
        when(productRepository.findById(cart1.getProductId())).thenReturn(Optional.of(product1));
        when(productRepository.findById(cart2.getProductId())).thenReturn(Optional.of(product2));

        // when
        List<ProductResponseDto> result = cartService.getProductsInCart(memberId);

        // then
        assertEquals(2, result.size());
        assertEquals(product1.getName(), result.get(0).name());
        assertEquals(product2.getName(), result.get(1).name());
    }

    @Test
    @DisplayName("장바구니 상품 수량 변경 성공")
    void changeQuantityTest() {
        // given
        Long cartId = 1L;
        Long memberId = 2L;
        Long productId = 3L;
        int beforeQuantity = 3;
        int afterQuantity = 5;
        ChangeQuantityDto dto = new ChangeQuantityDto(cartId, afterQuantity);

        Cart cart = new Cart(cartId, memberId, productId, beforeQuantity);

        // when
        when(cartRepository.findById(cartId)).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenAnswer(invocation -> invocation.getArgument(0));

        cartService.changeQuantity(dto);

        // then
        verify(cartRepository).save(cart);
        assertEquals(afterQuantity, cart.getQuantity());
    }

}