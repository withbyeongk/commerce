package io.hhplus.commerce.application.service.product;

import io.hhplus.commerce.common.exception.CommerceErrorCodes;
import io.hhplus.commerce.common.exception.CommerceException;
import io.hhplus.commerce.domain.product.Product;
import io.hhplus.commerce.domain.product.ProductStock;
import io.hhplus.commerce.infra.repository.product.ProductRepository;
import io.hhplus.commerce.infra.repository.product.ProductStockRepository;
import io.hhplus.commerce.presentation.controller.order.dto.OrderRequestDto;
import io.hhplus.commerce.presentation.controller.product.dto.ProductRequestDto;
import io.hhplus.commerce.presentation.controller.product.dto.ProductResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductStockRepository productStockRepository;

    public Page<ProductResponseDto> findAll(Pageable pageable) {
        return productRepository.findAll(pageable).map(ProductResponseDto::new);
    }

    public Long add(ProductRequestDto dto) {
        return productRepository.save(new Product(dto)).getId();
    }

    public Product getProduct(Long productId) {
        return productRepository.findById(productId).orElseThrow(() -> new CommerceException(CommerceErrorCodes.PRODUCT_NOT_FOUND));
    }

    public List<Product> findAllByIds(List<Long> ids) {
        return productRepository.findAllByIdIn(ids);
    }

    public List<ProductStock> findAllProductQuantityWithLock(List<Long> productIds) {
        return productStockRepository.findAllByIdIn(productIds);
    }

    public void updateStock(OrderRequestDto.OrderItemRequestDto dto) {
        ProductStock productStock = productStockRepository.findById(dto.productId()).orElseThrow(()-> new CommerceException(CommerceErrorCodes.PRODUCT_STOCK_NOT_FOUND));
        productStock.minus(dto.quantity());
        productStockRepository.save(productStock);

        Product product = productRepository.findById(dto.productId()).orElseThrow(()-> new CommerceException(CommerceErrorCodes.PRODUCT_NOT_FOUND));
        product.update(productStock.getStock());
        productRepository.save(product);
    }
}
