package io.hhplus.commerce.application.service;

import io.hhplus.commerce.infra.repository.PayRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PayService {
    private final PayRepository payRepository;
}
