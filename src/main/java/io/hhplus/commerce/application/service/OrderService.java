package io.hhplus.commerce.application.service;

import io.hhplus.commerce.domain.repository.OrderItemRepository;
import io.hhplus.commerce.domain.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
}
