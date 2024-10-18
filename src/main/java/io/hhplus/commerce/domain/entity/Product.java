package io.hhplus.commerce.domain.entity;

import io.hhplus.commerce.presentation.dto.ProductRequestDto;
import io.hhplus.commerce.presentation.dto.ProductResponseDto;
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
@Table(name = "product")
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_name", nullable = false, length = 20)
    private String name;

    @Column(name = "price", nullable = false)
    private int price;

    @Column(name = "stock", nullable = false)
    private int stock;

    @Column(name = "description", nullable = false, length = 200)
    private String description;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @CreatedDate
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public Product(String name, int price, int stock, String description) {
        this(name, price, stock, description, null, null, LocalDateTime.now());
    }

    public Product(String name, int price, int stock, String description, LocalDateTime deletedAt, LocalDateTime updatedAt, LocalDateTime now) {
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.description = description;
        this.deletedAt = deletedAt;
        this.updatedAt = updatedAt;
        this.createdAt = now;
    }

    public Product(ProductRequestDto dto) {
        this(dto.name(), dto.price(), dto.stock(), dto.description(), null, null, LocalDateTime.now());
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

    public ProductResponseDto toResponseDto() {
        return new ProductResponseDto(id, name, price, stock, description, createdAt);
    }

    public Product minusStock(int amount) {
        return new Product(name, price, stock - amount, description, deletedAt, updatedAt, createdAt);
    }
}