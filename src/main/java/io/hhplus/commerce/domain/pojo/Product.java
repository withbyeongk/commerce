package io.hhplus.commerce.domain.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class Product {
    private String name;
    private int price;
    private int stock;
    private String description;
    private LocalDateTime deletedAt;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;
}
