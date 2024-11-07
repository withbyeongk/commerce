package io.hhplus.commerce.presentation.controller.product.dto;

import java.io.Serializable;
import java.util.List;


public record ProductListResponseDto(List<ProductResponseDto> products) implements Serializable {}