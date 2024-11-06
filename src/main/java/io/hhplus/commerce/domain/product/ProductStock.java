package io.hhplus.commerce.domain.product;

import io.hhplus.commerce.common.exception.CommerceErrorCodes;
import io.hhplus.commerce.common.exception.CommerceException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Objects;

@Entity
@Getter
@Table(name = "product_stock")
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
@EntityListeners(AuditingEntityListener.class)
public class ProductStock {
    @Id
    @Column(name = "product_id", nullable = false)
    private Long id;

    @Column(name = "stock", nullable = false)
    private int stock;

    @Version
    private int version;

    public ProductStock(Long id, int stock) {
        this.id = id;
        this.stock = stock;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductStock transStock = (ProductStock) o;
        return this.stock == transStock.stock &&
                Objects.equals(id, transStock.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, stock);
    }

    @Override
    public String toString() {
        return "ProductStock{" +
                "id=" + id +
                ", stock=" + stock +
                ", version=" + version +
                '}';
    }

    public void minus(int quantity) {
        if (quantity <= 0) {
            throw new CommerceException(CommerceErrorCodes.INVALID_ARGUMENTS_QUANTITY);
        }
        if (this.stock < quantity) {
            throw new CommerceException(CommerceErrorCodes.INSUFFICIENT_STOCK);
        }
        this.stock -= quantity;
        log.info("PRODUCT STOCK :: 상품 감소량 : {}, 상품 재고 : {}", quantity, this.stock);
    }

    public void plus(int quantity) {
        if (quantity <= 0) {
            throw new CommerceException(CommerceErrorCodes.INVALID_ARGUMENTS_QUANTITY);
        }
        this.stock += quantity;
        log.info("PRODUCT STOCK :: 상품 추가량 : {}, 상품 재고 : {}", quantity, this.stock);
    }

}