package io.hhplus.commerce.domain.entity;

import io.hhplus.commerce.common.exception.CommerceErrorCodes;
import io.hhplus.commerce.common.exception.CommerceException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
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
            throw new CommerceException(CommerceErrorCodes.INVALID_ARGUMENTS_POINT);
        }
        this.point += point;
        log.info("POINT :: 충전금액 : {}, 잔액 : {}", point, this.point);
    }

    public void use(int point) {
        if (point < 1) {
            throw new CommerceException(CommerceErrorCodes.INVALID_ARGUMENTS_POINT);
        }
        this.point -= point;
        log.info("POINT :: 사용금액 : {}, 잔액 : {}", point, this.point);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point transPoint = (Point) o;
        return this.point == transPoint.point &&
                Objects.equals(memberId, transPoint.memberId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(memberId, point);
    }

}
