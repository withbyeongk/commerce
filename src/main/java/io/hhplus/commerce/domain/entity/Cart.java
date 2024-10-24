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
@Table(name = "cart")
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Cart {

    private static final int DEFAULT_QUANTITY = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Long id;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "quantity", nullable = false)
    private int quantity = DEFAULT_QUANTITY;

    public Cart(Long memberId, Long productId) {
        this.memberId = memberId;
        this.productId = productId;
        this.quantity = DEFAULT_QUANTITY;
    }

    public Cart(Long memberId, Long productId, int quantity) {
        this.memberId = memberId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public Cart(Long id, Long memberId, Long productId) {
        this.id = id;
        this.memberId = memberId;
        this.productId = productId;
    }

    public void plus(int quantity) {
        if (quantity <= 0) {
            throw new CommerceException(CommerceErrorCodes.INVALID_ARGUMENTS_QUANTITY);
        }
        this.quantity += quantity;
    }

    public void minus(int quantity) {
        if (quantity <= 0) {
            throw new CommerceException(CommerceErrorCodes.INVALID_ARGUMENTS_QUANTITY);
        }

        if (this.quantity <= quantity) {
            throw new CommerceException(CommerceErrorCodes.INVALID_EXCEED_QUANTITY);
        }

        this.quantity -= quantity;
    }

    public void changeQuantity(int quantity) {
        if (quantity <= 0) {
            throw new CommerceException(CommerceErrorCodes.INVALID_ARGUMENTS_QUANTITY);
        }
        this.quantity = quantity;
    }
}