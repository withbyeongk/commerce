package io.hhplus.commerce.application.facade.usecase;

import io.hhplus.commerce.presentation.controller.product.dto.ProductRequestDto;
import io.hhplus.commerce.presentation.controller.product.dto.ProductResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductUsecase {

    List<ProductResponseDto> getBestSellers();

    ProductResponseDto getProduct(Long productId);

    Page<ProductResponseDto> findAll(Pageable pageable);

    Long add(ProductRequestDto dto);

}
