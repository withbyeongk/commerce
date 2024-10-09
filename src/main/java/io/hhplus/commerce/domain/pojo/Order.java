package io.hhplus.commerce.domain.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class Order {
    private Long memberId;
    private int totalPrice;
    private LocalDateTime deletedAt;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;
}
