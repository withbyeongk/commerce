package io.hhplus.commerce.domain.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderItem {
    private Long orderId;
    private Long productId;
    private int amount;
}
