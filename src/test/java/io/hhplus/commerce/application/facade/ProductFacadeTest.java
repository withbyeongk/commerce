package io.hhplus.commerce.application.facade;

import io.hhplus.commerce.application.service.product.ProductService;
import io.hhplus.commerce.domain.order.Order;
import io.hhplus.commerce.domain.order.OrderItem;
import io.hhplus.commerce.infra.repository.order.OrderItemRepository;
import io.hhplus.commerce.infra.repository.order.OrderRepository;
import io.hhplus.commerce.presentation.controller.product.dto.ProductListResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.cache.CacheManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class ProductFacadeTest {
    @Autowired
    private CacheManager cacheManager;

    @SpyBean
    private ProductFacade productFacade;

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Test
    @DisplayName("베스트상품 조회시 캐시에 저장하는지 확인하는 테스트. 재조회 했을때 캐시를 사용하는지 확인한다.")
    void cacheTestWithBestSellers() {
        // given
        Order savedOrder = orderRepository.save(new Order(1L, 10000));
        orderItemRepository.save(new OrderItem(savedOrder.getId(), 35L, 1));
        orderItemRepository.save(new OrderItem(savedOrder.getId(), 36L, 2));
        orderItemRepository.save(new OrderItem(savedOrder.getId(), 37L, 3));
        orderItemRepository.save(new OrderItem(savedOrder.getId(), 38L, 4));
        orderItemRepository.save(new OrderItem(savedOrder.getId(), 39L, 5));

        ProductListResponseDto dto1 = productFacade.getBestSellers();
        ProductListResponseDto dto2 = productFacade.getBestSellers();
        ProductListResponseDto dto3 = productFacade.getBestSellers();

        assertNotNull(dto1);
        assertNotNull(dto2);
        assertNotNull(dto3);
        assertEquals(dto1.products().size(), dto2.products().size());
        assertEquals(dto2.products().size(), dto3.products().size());

        for (int i = 0; i < dto1.products().size(); i++) {
            assertEquals(dto1.products().get(i).productId(), dto2.products().get(i).productId());
            assertEquals(dto2.products().get(i).productId(), dto3.products().get(i).productId());
        }

        assertNotNull(cacheManager.getCache("redisCache"));
        verify(productFacade, times(1)).getBestSellers();

    }
}