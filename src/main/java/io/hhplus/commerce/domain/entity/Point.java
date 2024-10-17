package io.hhplus.commerce.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class Point {

    private static final int DEFAULT_POINTS = 0;

    @Id
    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "point", nullable = false)
    private int point;

    public Point() {
        throw new IllegalArgumentException("회원없이 포인트가 존재할 수 없습니다.");
    }

    public Point(Long memberId, int point) {
        this.memberId = memberId;
        this.point = point;
    }

    public Point(Long memberId) {
        this(memberId, DEFAULT_POINTS);
    }
}
