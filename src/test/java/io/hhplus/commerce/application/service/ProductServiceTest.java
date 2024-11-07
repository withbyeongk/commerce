package io.hhplus.commerce.application.service;

import io.hhplus.commerce.application.service.product.ProductService;
import io.hhplus.commerce.common.exception.CommerceErrorCodes;
import io.hhplus.commerce.common.exception.CommerceException;
import io.hhplus.commerce.infra.repository.product.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @InjectMocks
    ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Test
    @DisplayName("찾을 수 없는 상품ID를 입력 시 에러 발생")
    void productIdNotFoundError() {
        // given
        Long id = 1L;
        when(productRepository.findById(id)).thenReturn(Optional.empty());

        // expected
        CommerceException e = assertThrows(CommerceException.class, () -> {
            productService.getProduct(id);
        });

        // then
        assertEquals(CommerceErrorCodes.PRODUCT_NOT_FOUND, e.getErrorCode());
    }

}