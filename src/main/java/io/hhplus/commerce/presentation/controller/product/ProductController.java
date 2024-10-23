package io.hhplus.commerce.presentation.controller.product;

import io.hhplus.commerce.application.service.ProductService;
import io.hhplus.commerce.presentation.controller.product.dto.ProductRequestDto;
import io.hhplus.commerce.presentation.controller.product.dto.ProductResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "/api/products", description = "상품 API")
public class ProductController {
    private final ProductService productService;


    @Operation(summary = "상품 추가")
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Long.class)))
    @PostMapping("/add")
    public Long add(@RequestBody ProductRequestDto dto) {
        return productService.add(dto);
    }

    @Operation(summary = "상품 목록 조회")
    @ApiResponse(responseCode = "200", description = "성공", content = {@Content(
            mediaType = "application/json",
            array = @ArraySchema(schema = @Schema(implementation = ProductResponseDto.class))
    )})
    @GetMapping("/all")
    public Page<ProductResponseDto> findAll(Pageable pageable) {
        return productService.findAll(pageable);
    }

    @Operation(summary = "인기 상품 조회")
    @ApiResponse(responseCode = "200", description = "성공", content = {@Content(
            mediaType = "application/json",
            array = @ArraySchema(schema = @Schema(implementation = ProductResponseDto.class))
    )})
    @GetMapping("/bestsellers")
    public List<ProductResponseDto> getBestSellers() {
        return productService.findTopFiveWhileThreeDays();
    }

    @Operation(summary = "상품 상세 조회")
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(
            mediaType = "application/json", schema = @Schema(implementation = ProductResponseDto.class)))
    @GetMapping("/{productId}")
    public ProductResponseDto findById(@PathVariable (name = "productId") Long productId) {
        return productService.findById(productId);
    }
}
