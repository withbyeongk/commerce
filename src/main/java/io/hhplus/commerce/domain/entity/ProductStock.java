package io.hhplus.commerce.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@Table(name = "product_stock")
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ProductStock {
    @Id
    @Column(name = "product_id", nullable = false)
    private Long id;

    @Column(name = "stock", nullable = false)
    private int stock;

    public void minus(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("재고 감소 수량은 양수여야 합니다..");
        }
        if (this.stock < quantity) {
            throw new IllegalArgumentException("재고가 부족합니다.");
        }
        this.stock -= quantity;
    }

    public void plus(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("재고 추가 수량은 양수여야 합니다..");
        }
        this.stock += quantity;
    }

}