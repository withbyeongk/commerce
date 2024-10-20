package io.hhplus.commerce.presentation.dataflatform;

import io.hhplus.commerce.presentation.dto.OrderInfoToDataPlatformDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ReportOrderInfo {

    public void sendOrderInfomation(OrderInfoToDataPlatformDto dto) {
        log.info("외부 데이터 플랫폼으로 주문 데이터 전송");
    }
}
