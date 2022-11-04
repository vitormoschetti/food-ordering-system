package com.food.ordering.system.order.service.domain;

import com.food.ordering.system.order.service.domain.dto.track.TrackingOrderQuery;
import com.food.ordering.system.order.service.domain.dto.track.TrackingOrderResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderTrackCommandHandler {

    public TrackingOrderResponse trackOrder(final TrackingOrderQuery trackingOrderQuery) {
        return null;
    }

}
