package io.hhplus.commerce.domain.entity;

import io.hhplus.commerce.common.exception.CommerceErrorCodes;
import io.hhplus.commerce.common.exception.CommerceException;
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
            throw new CommerceException(CommerceErrorCodes.INVALID_ARGUMENTS_QUANTITY);
        }
        if (this.stock < quantity) {
            throw new CommerceException(CommerceErrorCodes.INSUFFICIENT_STOCK);
        }
        this.stock -= quantity;
    }

    public void plus(int quantity) {
        if (quantity <= 0) {
            throw new CommerceException(CommerceErrorCodes.INVALID_ARGUMENTS_QUANTITY);
        }
        this.stock += quantity;
    }

}