package io.hhplus.commerce.presentation.dto;

import io.hhplus.commerce.domain.product.Product;
import io.hhplus.commerce.presentation.controller.product.dto.ProductResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


class ProductResponseDtoTest {


    @Test
    @DisplayName("Product -> ProductResponseDto 로 잘 변환되는지 테스트합니다.")
    void toDtoTest() {
        // given
        Product product = new Product(1L, "Product 1", 100, 50, "Description 1");

        // when
        ProductResponseDto productResponseDto = new ProductResponseDto(product);

        // then
        assertEquals(product.getId(), productResponseDto.productId());
        assertEquals(product.getName(), productResponseDto.name());
        assertEquals(product.getPrice(), productResponseDto.price());
        assertEquals(product.getStock(), productResponseDto.stock());
        assertEquals(product.getDescription(), productResponseDto.description());
        assertEquals(product.getCreatedAt(), productResponseDto.createdAt());
    }
}