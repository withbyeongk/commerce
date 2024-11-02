package io.hhplus.commerce.domain.entity;
import io.hhplus.commerce.presentation.controller.member.dto.PointResponseDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@Table(name = "member")
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
@EntityListeners(AuditingEntityListener.class)
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(name = "member_name", nullable = false, length = 20)
    private String name;

    @Column(name = "point", nullable = false)
    private int point;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @CreatedDate
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public Member(String name, int point) {
        this.name = name;
        this.point = point;
        this.createdAt = LocalDateTime.now();
    }

    public Member(Long id, String name, int point) {
        this.id = id;
        this.name = name;
        this.point = point;
        this.createdAt = LocalDateTime.now();
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public void charge(int points) {
        this.point += points;
        log.info("MEMBER :: 충전금액 : {}, 잔액 : {}", points, this.point);
    }

    public void use(int points) {
        this.point -= points;
        log.info("MEMBER ::.사용금액 : {}, 잔액 : {}", points, this.point);
    }

    public void update(int point) {
        this.point = point;
        log.info("MEMBER :: 업데이트 :: 잔액 : {}", this.point);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Member transMember = (Member) o;
        return point == transMember.point &&
                Objects.equals(id, transMember.id) &&
                Objects.equals(name, transMember.name) &&
                Objects.equals(deletedAt, transMember.deletedAt) &&
                Objects.equals(updatedAt, transMember.updatedAt) &&
                Objects.equals(createdAt, transMember.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, point, deletedAt, updatedAt, createdAt);
    }


    public PointResponseDto toResponseDto() {
        return new PointResponseDto(id, point);
    }
}