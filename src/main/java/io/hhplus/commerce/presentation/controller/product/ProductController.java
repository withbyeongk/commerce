package io.hhplus.commerce.presentation.controller.product;

import io.hhplus.commerce.application.facade.usecase.ProductUsecase;
import io.hhplus.commerce.presentation.controller.product.dto.ProductListResponseDto;
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


@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "/api/products", description = "상품 API")
public class ProductController {
    private final ProductUsecase productUsecase;


    @Operation(summary = "상품 추가")
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Long.class)))
    @PostMapping("/add")
    public Long add(@RequestBody ProductRequestDto dto) {
        return productUsecase.add(dto);
    }

    @Operation(summary = "상품 목록 조회")
    @ApiResponse(responseCode = "200", description = "성공", content = {@Content(
            mediaType = "application/json",
            array = @ArraySchema(schema = @Schema(implementation = ProductResponseDto.class))
    )})
    @GetMapping("/all")
    public Page<ProductResponseDto> findAll(Pageable pageable) {
        return productUsecase.findAll(pageable);
    }

    @Operation(summary = "인기 상품 조회")
    @ApiResponse(responseCode = "200", description = "성공", content = {@Content(
            mediaType = "application/json",
            array = @ArraySchema(schema = @Schema(implementation = ProductListResponseDto.class))
    )})
    @GetMapping("/bestsellers")
    public ProductListResponseDto getBestSellers() {
        return productUsecase.getBestSellers();
    }

    @Operation(summary = "상품 상세 조회")
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(
            mediaType = "application/json", schema = @Schema(implementation = ProductResponseDto.class)))
    @GetMapping("/{productId}")
    public ProductResponseDto findById(@PathVariable (name = "productId") Long productId) {
        return productUsecase.getProduct(productId);
    }
}
