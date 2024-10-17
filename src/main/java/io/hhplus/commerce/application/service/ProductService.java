package io.hhplus.commerce.application.service;

import io.hhplus.commerce.domain.entity.Product;
import io.hhplus.commerce.infra.repository.ProductRepository;
import io.hhplus.commerce.presentation.dto.ProductRequestDto;
import io.hhplus.commerce.presentation.dto.ProductResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public Page<ProductResponseDto> findAll(Pageable pageable) {
        return productRepository.findAll(pageable).map(ProductResponseDto::new);
    }

    public Long add(ProductRequestDto dto) {
        return productRepository.save(new Product(dto)).getId();
    }

    public ProductResponseDto findById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return new ProductResponseDto(product);
    }

    public List<ProductResponseDto> findTopFiveWhileThreeDays() {
        List<Product> products = productRepository.findTopFiveWhileThreeDays();
        return products.stream()
                .map(ProductResponseDto::new)
                .collect(Collectors.toList());
    }
}
