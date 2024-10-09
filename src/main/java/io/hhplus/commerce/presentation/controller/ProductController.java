package io.hhplus.commerce.presentation.controller;

import io.hhplus.commerce.application.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

}
