package io.hhplus.commerce.application.service.order;

import io.hhplus.commerce.domain.order.Payment;
import io.hhplus.commerce.infra.repository.order.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;


    public Payment payment(Payment payment) {
        return paymentRepository.save(payment);
    }
}
