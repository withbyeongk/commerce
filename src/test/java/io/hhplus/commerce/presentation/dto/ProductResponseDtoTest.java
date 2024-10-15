package io.hhplus.commerce.presentation.dto;

import io.hhplus.commerce.domain.entity.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;


class ProductResponseDtoTest {


    @Test
    @DisplayName("Product -> ProductResponseDto 로 잘 변환되는지 테스트합니다.")
    void toDtoTest() {
        // given
        Product product = new Product(1L, "Product 1", 100, 50, "Description 1", null, LocalDateTime.now(), LocalDateTime.now());

        // when
        ProductResponseDto productResponseDto = new ProductResponseDto(product);

        // then
        assertEquals(product.getId(), productResponseDto.productId());
        assertEquals(product.getName(), productResponseDto.name());
        assertEquals(product.getPrice(), productResponseDto.price());
        assertEquals(product.getStock(), productResponseDto.stock());
        assertEquals(product.getDescription(), productResponseDto.description());
        assertEquals(product.getDeletedAt(), productResponseDto.deletedAt());
        assertEquals(product.getUpdatedAt(), productResponseDto.updatedAt());
        assertEquals(product.getCreatedAt(), productResponseDto.createdAt());
    }
}