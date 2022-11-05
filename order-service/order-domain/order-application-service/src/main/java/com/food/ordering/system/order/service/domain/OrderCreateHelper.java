package com.food.ordering.system.order.service.domain;

import com.food.ordering.system.order.service.domain.dto.create.CreateOrderCommand;
import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.entity.Restaurant;
import com.food.ordering.system.order.service.domain.event.OrderCreatedEvent;
import com.food.ordering.system.order.service.domain.exception.OrderDomainException;
import com.food.ordering.system.order.service.domain.mapper.OrderDataMapper;
import com.food.ordering.system.order.service.domain.ports.output.repository.CustomerRepository;
import com.food.ordering.system.order.service.domain.ports.output.repository.OrderRepository;
import com.food.ordering.system.order.service.domain.ports.output.repository.RestaurantRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

@Slf4j
@Component
@AllArgsConstructor
public class OrderCreateHelper {

    private final OrderDomainService orderDomainService;
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final RestaurantRepository restaurantRepository;
    private final OrderDataMapper orderDataMapper;

    @Transactional
    public OrderCreatedEvent persistOrder(final CreateOrderCommand command) {
        this.checkCustomer(command.getCustomerId());
        final var restaurant = this.checkRestaurant(command);
        final var order = this.orderDataMapper.createOrderCommandToOrder(command);
        final var orderCreatedEvent = this.orderDomainService.validateAndInitiateOrder(order, restaurant);
        final var orderResult = this.saveOrder(order);
        log.info("Order is created with id: {}", orderResult.getId().getValue());
        return orderCreatedEvent;
    }

    private Restaurant checkRestaurant(CreateOrderCommand command) {
        final var restaurant = this.orderDataMapper.createOrderCommandToRestaurant(command);
        final var restaurantOptional = this.restaurantRepository.findRestaurantInformation(restaurant);
        if (restaurantOptional.isEmpty()) {
            log.warn("Could not find restaurant with restaurant id: {}", command.getRestaurantId());
            throw new OrderDomainException("Could not find restaurant with restaurant id: " + command.getRestaurantId());
        }
        return restaurantOptional.get();
    }

    private void checkCustomer(UUID customerId) {
        final var customer = this.customerRepository.findCustomer(customerId);
        if (customer.isEmpty()) {
            log.warn("Could not find customer with customer id: {}", customerId);
            throw new OrderDomainException("Could not find customer with customer id: " + customerId);
        }
    }

    private Order saveOrder(Order order) {
        final var orderResult = this.orderRepository.save(order);
        if (Objects.isNull(orderResult)) {
            log.error("Could not save order!");
            throw new OrderDomainException("Could not save order!");
        }
        log.info("Order is saved with id: {}", orderResult.getId());
        return orderResult;
    }

}
