package io.hhplus.commerce.application.util;

import io.hhplus.commerce.domain.entity.Product;
import io.hhplus.commerce.presentation.dto.ProductResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.stream.Collectors;

public class Converter {

    public static ProductResponseDto convertToDto(Product product) {
        return new ProductResponseDto(product);
    }

    public static Page<ProductResponseDto> convertToDto(Page<Product> products) {
        return new PageImpl<>(products.getContent().stream().map(Converter::convertToDto).collect(Collectors.toList()));
    }
}
