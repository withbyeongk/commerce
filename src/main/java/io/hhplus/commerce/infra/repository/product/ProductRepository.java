package io.hhplus.commerce.infra.repository.product;

import io.hhplus.commerce.domain.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    public List<Product> findAllByIdIn(List<Long> products);
}
