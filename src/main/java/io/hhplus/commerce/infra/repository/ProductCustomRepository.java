package io.hhplus.commerce.infra.repository;

import io.hhplus.commerce.domain.entity.Product;

import java.util.List;

public interface ProductCustomRepository {
    List<Product> findTopFiveWhileThreeDays();
}
