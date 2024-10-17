package io.hhplus.commerce.infra.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import io.hhplus.commerce.domain.entity.Product;
import io.hhplus.commerce.domain.entity.QOrder;
import io.hhplus.commerce.domain.entity.QOrderItem;
import io.hhplus.commerce.domain.entity.QProduct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProductCustomRepositoryImpl implements ProductCustomRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Product> findTopFiveWhileThreeDays() {
        QOrderItem orderItem = QOrderItem.orderItem;
        QOrder order = QOrder.order;
        QProduct product = QProduct.product;
        LocalDateTime threeDaysAgo = LocalDateTime.now().minusDays(3);

        List<Long> topProductIds = queryFactory.select(orderItem.productId)
                .from(orderItem)
                .join(order).on(order.id.eq(orderItem.orderId))
                .where(order.createdAt.between(threeDaysAgo, LocalDateTime.now()))
                .groupBy(orderItem.productId)
                .orderBy(orderItem.amount.sum().desc())
                .limit(5)
                .fetch();

        return queryFactory.selectFrom(product)
                .where(product.id.in(topProductIds))
                .fetch();
    }
}