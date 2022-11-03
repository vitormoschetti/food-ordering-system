package com.food.ordering.system.order.service.domain.ports.input.service;

import com.food.ordering.system.order.service.domain.dto.create.CreateOrderCommand;
import com.food.ordering.system.order.service.domain.dto.create.CreateOrderResponse;
import com.food.ordering.system.order.service.domain.dto.track.TrackingOrderQuery;
import com.food.ordering.system.order.service.domain.dto.track.TrackingOrderResponse;

import javax.validation.Valid;

public interface OrderApplicationService {

    CreateOrderResponse createOrder(@Valid final CreateOrderCommand createOrderCommand);

    TrackingOrderResponse trackOrder(@Valid final TrackingOrderQuery trackingOrderQuery);

}
