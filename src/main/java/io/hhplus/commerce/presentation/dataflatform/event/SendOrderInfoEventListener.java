package io.hhplus.commerce.presentation.dataflatform.event;

import io.hhplus.commerce.presentation.dataflatform.ReportOrderInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class SendOrderInfoEventListener {

    private final ReportOrderInfo orderInfo;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onSendOrderInfoEvent(OrderCompletedEvent event) {
        System.out.println("onSendOrderInfoEvent :: " + Thread.currentThread().toString());
        orderInfo.sendOrderInfomation(event);
    }
}
