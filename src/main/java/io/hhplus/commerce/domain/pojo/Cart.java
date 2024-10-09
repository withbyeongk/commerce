package io.hhplus.commerce.domain.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Cart {
    private Long memberId;
    private Long productId;
    private int amount;
}
