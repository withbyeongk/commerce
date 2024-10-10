package io.hhplus.commerce.presentation.controller;

import io.hhplus.commerce.application.service.CartService;
import io.hhplus.commerce.presentation.dto.ProductResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @GetMapping("/all")
    public List<ProductResponseDTO> findAll() {
        return getMock20();
    }

    @GetMapping("/bestsellers")
    public List<ProductResponseDTO> getBestSellers() {
        return getMockBestSellers();
    }

    @GetMapping("{product_id}")
    public ProductResponseDTO findById(Long productId) {
        return new ProductResponseDTO(1L, "Product 1", 100, 50, "Description 1", null, LocalDateTime.now(), LocalDateTime.now());
    }

    private List<ProductResponseDTO> getMockBestSellers() {
        return List.of(
                new ProductResponseDTO(1L, "Product 1", 100, 50, "Description 1", null, LocalDateTime.now(), LocalDateTime.now()),
                new ProductResponseDTO(2L, "Product 2", 200, 100, "Description 2", null, LocalDateTime.now(), LocalDateTime.now()),
                new ProductResponseDTO(3L, "Product 3", 300, 150, "Description 3", null, LocalDateTime.now(), LocalDateTime.now()),
                new ProductResponseDTO(4L, "Product 4", 400, 200, "Description 4", null, LocalDateTime.now(), LocalDateTime.now()),
                new ProductResponseDTO(5L, "Product 5", 500, 250, "Description 5", null, LocalDateTime.now(), LocalDateTime.now())
        );
    }

    private List<ProductResponseDTO> getMock20() {
        return List.of(
                new ProductResponseDTO(1L, "Product 1", 100, 50, "Description 1", null, LocalDateTime.now(), LocalDateTime.now()),
                new ProductResponseDTO(2L, "Product 2", 200, 100, "Description 2", null, LocalDateTime.now(), LocalDateTime.now()),
                new ProductResponseDTO(3L, "Product 3", 300, 150, "Description 3", null, LocalDateTime.now(), LocalDateTime.now()),
                new ProductResponseDTO(4L, "Product 4", 400, 200, "Description 4", null, LocalDateTime.now(), LocalDateTime.now()),
                new ProductResponseDTO(5L, "Product 5", 500, 250, "Description 5", null, LocalDateTime.now(), LocalDateTime.now()),
                new ProductResponseDTO(6L, "Product 6", 600, 300, "Description 6", null, LocalDateTime.now(), LocalDateTime.now()),
                new ProductResponseDTO(7L, "Product 7", 700, 350, "Description 7", null, LocalDateTime.now(), LocalDateTime.now()),
                new ProductResponseDTO(8L, "Product 8", 800, 400, "Description 8", null, LocalDateTime.now(), LocalDateTime.now()),
                new ProductResponseDTO(9L, "Product 9", 900, 450, "Description 9", null, LocalDateTime.now(), LocalDateTime.now()),
                new ProductResponseDTO(10L, "Product 10", 1000, 500, "Description 10", null, LocalDateTime.now(), LocalDateTime.now()),
                new ProductResponseDTO(11L, "Product 11", 1100, 550, "Description 11", null, LocalDateTime.now(), LocalDateTime.now()),
                new ProductResponseDTO(12L, "Product 12", 1200, 600, "Description 12", null, LocalDateTime.now(), LocalDateTime.now()),
                new ProductResponseDTO(13L, "Product 13", 1300, 650, "Description 13", null, LocalDateTime.now(), LocalDateTime.now()),
                new ProductResponseDTO(14L, "Product 14", 1400, 700, "Description 14", null, LocalDateTime.now(), LocalDateTime.now()),
                new ProductResponseDTO(15L, "Product 15", 1500, 750, "Description 15", null, LocalDateTime.now(), LocalDateTime.now()),
                new ProductResponseDTO(16L, "Product 16", 1600, 800, "Description 16", null, LocalDateTime.now(), LocalDateTime.now()),
                new ProductResponseDTO(17L, "Product 17", 1700, 850, "Description 17", null, LocalDateTime.now(), LocalDateTime.now()),
                new ProductResponseDTO(18L, "Product 18", 1800, 900, "Description 18", null, LocalDateTime.now(), LocalDateTime.now()),
                new ProductResponseDTO(19L, "Product 19", 1900, 950, "Description 19", null, LocalDateTime.now(), LocalDateTime.now()),
                new ProductResponseDTO(20L, "Product 20", 2000, 1000, "Description 20", null, LocalDateTime.now(), LocalDateTime.now())
        );
    }
}
