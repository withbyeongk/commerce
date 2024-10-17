package io.hhplus.commerce.application.service;

import io.hhplus.commerce.infra.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @InjectMocks
    ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @BeforeEach
    void setup() {

    }

    @Test
    @DisplayName("찾을 수 없는 상품ID를 입력 시 에러 발생")
    void productIdNotFoundError() {
        // given
        Long id = 1L;
        when(productRepository.findById(id)).thenReturn(Optional.empty());

        // when & then
        assertThrows(RuntimeException.class, () -> productService.findById(id));
    }

}