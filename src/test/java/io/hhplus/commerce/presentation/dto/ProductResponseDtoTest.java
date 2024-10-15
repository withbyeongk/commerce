package io.hhplus.commerce.presentation.dto;

import io.hhplus.commerce.domain.entity.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;


class ProductResponseDtoTest {


    @Test
    @DisplayName("Product -> ProductResponseDto 로 잘 변환되는지 테스트합니다.")
    void toDtoTest() {
        // given
        Product product = new Product(1L, "Product 1", 100, 50, "Description 1", null, LocalDateTime.now(), LocalDateTime.now());

        // when
        ProductResponseDto productResponseDto = new ProductResponseDto(product);

        // then
        assert productResponseDto.productId() == product.getProductId();
        assert productResponseDto.name() == product.getName();
        assert productResponseDto.price() == product.getPrice();
        assert productResponseDto.stock() == product.getStock();
        assert productResponseDto.description() == product.getDescription();
        assert productResponseDto.deletedAt() == product.getDeletedAt();
        assert productResponseDto.updatedAt() == product.getUpdatedAt();
        assert productResponseDto.createdAt() == product.getCreatedAt();
    }
}