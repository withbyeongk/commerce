package io.hhplus.commerce.domain.entity;

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
            throw new IllegalArgumentException("상품 수를 추가할 때는 양수가 입력되어야 합니다.");
        }
        this.quantity += quantity;
    }

    public void minus(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("상품 수를 줄일 때는 양수가 입력되어야 합니다.");
        }

        if (this.quantity <= quantity) {
            throw new IllegalArgumentException("현재 장바구니에 담긴 상품 수보다 더 많거나 같은 양을 꺼낼 수 없습니다.");
        }

        this.quantity -= quantity;
    }

    public void changeQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("상품 수를 변경할 때는 양수가 입력되어야 합니다.");
        }
        this.quantity = quantity;
    }
}