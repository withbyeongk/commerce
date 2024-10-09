package io.hhplus.commerce.domain.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class Member {
    private String name;
    private int point;
    private LocalDateTime deletedAt;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;
}
