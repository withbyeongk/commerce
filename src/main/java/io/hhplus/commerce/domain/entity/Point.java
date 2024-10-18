package io.hhplus.commerce.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Point {

    private static final int DEFAULT_POINTS = 0;

    @Id
    @Column(name = "member_id")
    private Long memberId;

    @Version
    private int version;

    @Column(name = "point", nullable = false)
    private int point;

    public Point(Long memberId, int point) {
        this.memberId = memberId;
        this.point = point;
    }

    public Point(Long memberId) {
        this(memberId, DEFAULT_POINTS);
    }

    public void charge(int point) {
        if (point < 1) {
            throw new IllegalArgumentException("충전 포인트는 양수여야 합니다.");
        }
        this.point += point;
    }
}
