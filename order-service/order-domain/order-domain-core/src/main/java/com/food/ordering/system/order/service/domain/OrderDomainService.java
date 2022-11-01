package com.food.ordering.system.order.service.domain;

import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.entity.Restaurant;
import com.food.ordering.system.order.service.domain.event.OrderCancelledEvent;
import com.food.ordering.system.order.service.domain.event.OrderCreatedEvent;
import com.food.ordering.system.order.service.domain.event.OrderPaidEvent;

import java.util.List;

public interface OrderDomainService {

    OrderCreatedEvent validateAndInitiateOrder(final Order order, final Restaurant restaurant);

    OrderPaidEvent payOrder(final Order order);

    void approvedOrder(final Order order);

    OrderCancelledEvent cancelOrderPaymente(final Order order, final List<String> failureMessages);

    void cancelOrder(final Order order, final List<String> failureMessages);

}
