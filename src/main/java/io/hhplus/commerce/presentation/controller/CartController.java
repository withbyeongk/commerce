package io.hhplus.commerce.presentation.controller;

import io.hhplus.commerce.application.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;
}
