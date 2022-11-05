package com.food.ordering.system.order.service.domain;

import com.food.ordering.system.order.service.domain.dto.create.CreateOrderCommand;
import com.food.ordering.system.order.service.domain.dto.create.CreateOrderResponse;
import com.food.ordering.system.order.service.domain.mapper.OrderDataMapper;
import com.food.ordering.system.order.service.domain.ports.output.message.publisher.payment.OrderCreatedPaymentRequestMessagePublisher;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@AllArgsConstructor
public class OrderCreateCommandHandler {

    private final OrderCreateHelper orderCreateHelper;
    private final OrderDataMapper orderDataMapper;
    private final OrderCreatedPaymentRequestMessagePublisher publisher;

    public CreateOrderResponse createOrder(final CreateOrderCommand command) {
        final var orderCreatedEvent = this.orderCreateHelper.persistOrder(command);
        log.info("Order is created with id: {}", orderCreatedEvent.getOrder().getId().getValue());
        publisher.publish(orderCreatedEvent);
        return this.orderDataMapper.orderToCreateOrderResponse(orderCreatedEvent.getOrder());
    }


}
