package io.hhplus.commerce.application.service;

import io.hhplus.commerce.application.util.Converter;
import io.hhplus.commerce.domain.entity.Product;
import io.hhplus.commerce.infra.repository.ProductRepository;
import io.hhplus.commerce.presentation.dto.ProductResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public Page<ProductResponseDto> findAll(Pageable pageable) {
        return Converter.convertToDto(productRepository.findAll(pageable));
    }

}
