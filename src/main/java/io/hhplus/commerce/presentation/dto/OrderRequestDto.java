package io.hhplus.commerce.presentation.dto;

import java.util.List;

public record OrderRequestDto(
    Long memberId,
    List<Long> products
) {
}
