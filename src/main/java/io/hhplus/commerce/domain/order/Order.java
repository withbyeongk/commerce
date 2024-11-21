package io.hhplus.commerce.domain.order;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "orders")
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "total_price", nullable = false)
    private int totalPrice;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @CreatedDate
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public Order(Long id, Long memberId, int totalPrice) {
        this.id = id;
        this.memberId = memberId;
        this.totalPrice = totalPrice;
        this.status = OrderStatus.ORDERE_PENDING;
        this.createdAt = LocalDateTime.now();
    }

    public Order(Long memberId, int totalPrice) {
        this.memberId = memberId;
        this.totalPrice = totalPrice;
        this.status = OrderStatus.ORDERE_PENDING;
        this.createdAt = LocalDateTime.now();
    }

    public Order(Long id, int totalPrice, OrderStatus status) {
        this.id = id;
        this.totalPrice = totalPrice;
        this.status = status;
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

    public void updateStatus(OrderStatus status) {
        this.status = status;
    }

}