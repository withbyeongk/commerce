package io.hhplus.commerce.domain.order;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "payment")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "amount", nullable = false)
    private int amount;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @CreatedDate
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public Payment(Long orderId, Long memberId, int amount) {
        this.orderId = orderId;
        this.memberId = memberId;
        this.amount = amount;
    }

    public Payment(Long id, Long orderId, Long memberId, int amount) {
        this.id = id;
        this.orderId = orderId;
        this.memberId = memberId;
        this.amount = amount;
    }
}
