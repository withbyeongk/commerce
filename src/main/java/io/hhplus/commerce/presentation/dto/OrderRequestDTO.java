package io.hhplus.commerce.presentation.dto;

import java.util.List;

public record OrderRequestDTO(
    Long memberId,
    List<Long> products
) {
}
