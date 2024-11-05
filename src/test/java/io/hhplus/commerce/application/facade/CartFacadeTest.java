package io.hhplus.commerce.application.facade;

import io.hhplus.commerce.application.service.member.CartService;
import io.hhplus.commerce.application.service.member.MemberService;
import io.hhplus.commerce.application.service.product.ProductService;
import io.hhplus.commerce.domain.member.Cart;
import io.hhplus.commerce.domain.product.Product;
import io.hhplus.commerce.presentation.controller.member.dto.CartListDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CartFacadeTest {
    @Mock
    private CartService cartService;

    @Mock
    private ProductService productService;

    @Mock
    private MemberService memberService;

    @InjectMocks
    private CartFacade cartFacade;

    @Test
    void testGetProductsInCart() {
        // Given
        Long memberId = 1L;
        List<Cart> carts = Arrays.asList(
                new Cart(1L, memberId, 1L, 2),
                new Cart(2L, memberId, 2L, 3)
        );
        List<Product> products = Arrays.asList(
                new Product(1L, "product1", 1000, 2, "description1"),
                new Product(2L, "product2", 2000, 3, "description2")
        );

        when(cartService.findByMemberId(memberId)).thenReturn(carts);
        when(productService.findAllByIds(anyList())).thenReturn(products);

        // When
        List<CartListDto> actualProductDtos = cartFacade.getProductsInCart(memberId);

        // Then
        assertThat(actualProductDtos).hasSize(2);
        CartListDto firstDto = actualProductDtos.get(0);
        assertThat(firstDto.productId()).isEqualTo(1L);
        assertThat(firstDto.quantity()).isEqualTo(2);

        CartListDto secondDto = actualProductDtos.get(1);
        assertThat(secondDto.productId()).isEqualTo(2L);
        assertThat(secondDto.quantity()).isEqualTo(3);

        verify(cartService, times(1)).findByMemberId(memberId);
        verify(productService, times(1)).findAllByIds(anyList());
    }
}