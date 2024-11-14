package io.hhplus.commerce.application.facade;

import io.hhplus.commerce.application.facade.usecase.ProductUsecase;
import io.hhplus.commerce.application.service.order.OrderService;
import io.hhplus.commerce.application.service.product.ProductService;
import io.hhplus.commerce.domain.product.Product;
import io.hhplus.commerce.presentation.controller.product.dto.ProductListResponseDto;
import io.hhplus.commerce.presentation.controller.product.dto.ProductRequestDto;
import io.hhplus.commerce.presentation.controller.product.dto.ProductResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductFacade implements ProductUsecase {
    private final ProductService productService;
    private final OrderService orderService;
    private final CacheManager cacheManager;
    private static final int BESTSELLER_DAY = 3;
    private static final int BESTSELLER_COUNT = 5;

    @Override
    public ProductListResponseDto getBestSellers() {
        Cache cache = cacheManager.getCache("bestSellerCache");
        if (cache != null) {
            ProductListResponseDto cachedData = cache.get("bestSellerCache", ProductListResponseDto.class);
            if (cachedData != null) {
                return cachedData;
            }
        }

        List<Long> productIds = orderService.getBestSellersProductIds(BESTSELLER_DAY, BESTSELLER_COUNT);
        List<Product> products = productService.findAllByIds(productIds);

        List<ProductResponseDto> responseDtos = products.stream()
                .map(ProductResponseDto::new)
                .collect(Collectors.toList());

        ProductListResponseDto productListResponseDto = new ProductListResponseDto(responseDtos);
        if (cache != null && productListResponseDto != null) {
            cache.put("bestSellerCache", productListResponseDto);
        }
        return productListResponseDto;
    }

    @Override
    public ProductResponseDto getProduct(Long productId) {
        return productService.getProduct(productId).toResponseDto();
    }

    @Override
    public Page<ProductResponseDto> findAll(Pageable pageable) {
        return productService.findAll(pageable);
    }

    @Override
    public Long add(ProductRequestDto dto) {
        return productService.add(dto);
    }
}
