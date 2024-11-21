package io.hhplus.commerce.presentation.dataflatform.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.time.Clock;


@Getter
public class OrderCompletedEvent extends ApplicationEvent {
    private final Long orderId;
    private final Integer totalAmount;


    public OrderCompletedEvent(Object source, Long orderId, Integer totalAmount) {
        super(source);
        this.orderId = orderId;
        this.totalAmount = totalAmount;
    }

    public OrderCompletedEvent(Object source, Clock clock, Long orderId, Integer totalAmount) {
        super(source, clock);
        this.orderId = orderId;
        this.totalAmount = totalAmount;
    }
}
