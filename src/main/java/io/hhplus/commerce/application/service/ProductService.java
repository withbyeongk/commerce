package io.hhplus.commerce.application.service;

import io.hhplus.commerce.application.util.Converter;
import io.hhplus.commerce.infra.repository.ProductRepository;
import io.hhplus.commerce.presentation.dto.ProductRequestDto;
import io.hhplus.commerce.presentation.dto.ProductResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public Page<ProductResponseDto> findAll(Pageable pageable) {
        return Converter.convertToDto(productRepository.findAll(pageable));
    }

    public Long add(ProductRequestDto dto) {
        return productRepository.save(Converter.convertToEntity(dto)).getId();
    }
}
