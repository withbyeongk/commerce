package io.hhplus.commerce.presentation.controller.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record PointResponseDto(
    @Schema(description = "회원 ID")
    Long memberId,
    @Schema(description = "잔액")
    int point
) {
}
