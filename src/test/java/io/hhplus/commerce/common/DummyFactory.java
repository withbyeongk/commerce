package io.hhplus.commerce.common;

import io.hhplus.commerce.domain.member.Cart;
import io.hhplus.commerce.domain.member.Member;
import io.hhplus.commerce.domain.member.Point;
import io.hhplus.commerce.domain.product.Product;
import io.hhplus.commerce.domain.product.ProductStock;

public class DummyFactory {
    public static Member createMember() {
        return new Member("회원1");
    }
    public static Point createPoint(Long memberId) {
        return new Point(memberId, 10000);
    }

    public static Product createProduct() {
        return new Product("상품1", 100, 100, "상품1설명");
    }
    public static ProductStock createProductStock(Long productId) {
        return new ProductStock(productId, 100);
    }

    public static Cart createCart(Long memberId, Long productId) {
        return new Cart(null, memberId, productId, 1);
    }
}

