package io.hhplus.commerce.application.facade.usecase;

import io.hhplus.commerce.presentation.controller.product.dto.ProductListResponseDto;
import io.hhplus.commerce.presentation.controller.product.dto.ProductRequestDto;
import io.hhplus.commerce.presentation.controller.product.dto.ProductResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductUsecase {

    ProductListResponseDto getBestSellers();

    ProductResponseDto getProduct(Long productId);

    Page<ProductResponseDto> findAll(Pageable pageable);

    Long add(ProductRequestDto dto);

}
