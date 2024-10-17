package io.hhplus.commerce.presentation.controller;

import io.hhplus.commerce.application.service.ProductService;
import io.hhplus.commerce.presentation.dto.ProductRequestDto;
import io.hhplus.commerce.presentation.dto.ProductResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;


    @PostMapping("/add")
    public Long add(@RequestBody ProductRequestDto dto) {
        return productService.add(dto);
    }

    @GetMapping("/all")
    public Page<ProductResponseDto> findAll(Pageable pageable) {
        return productService.findAll(pageable);
    }

    @GetMapping("/bestsellers")
    public List<ProductResponseDto> getBestSellers() {
        return productService.findTopFiveWhileThreeDays();
    }

    @GetMapping("/{productId}")
    public ProductResponseDto findById(@PathVariable (name = "productId") Long productId) {
        return productService.findById(productId);
    }
}
