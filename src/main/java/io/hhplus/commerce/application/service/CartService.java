package io.hhplus.commerce.application.service;

import io.hhplus.commerce.infra.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;

}
