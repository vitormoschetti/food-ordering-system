package com.food.ordering.system.order.service.messaging.mapper;

import com.food.ordering.system.kafka.order.avro.model.*;
import com.food.ordering.system.order.service.domain.dto.message.PaymentResponse;
import com.food.ordering.system.order.service.domain.dto.message.RestaurantApprovalResponse;
import com.food.ordering.system.order.service.domain.event.OrderCancelledEvent;
import com.food.ordering.system.order.service.domain.event.OrderCreatedEvent;
import com.food.ordering.system.order.service.domain.event.OrderPaidEvent;
import com.food.ordering.system.order.service.domain.valueobject.OrderApprovalStatus;
import com.food.ordering.system.order.service.domain.valueobject.PaymentStatus;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class OrderMessagingDataMapper {

    public PaymentRequestAvroModel orderCreatedEventToPaymentRequestAvroModel(final OrderCreatedEvent event) {
        final var order = event.getOrder();
        final var createdAt = event.getCreatedAt();

        return PaymentRequestAvroModel.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setSagaId("")
                .setCustomerId(order.getCustomerId().getValue().toString())
                .setOrderId(order.getId().getValue().toString())
                .setPrice(order.getPrice().getAmount())
                .setCreatedAt(createdAt.toInstant())
                .setPaymentOrderStatus(PaymentOrderStatus.PENDING)
                .build();
    }

    public PaymentRequestAvroModel orderCancelledEventToPaymentRequestAvroModel(final OrderCancelledEvent event) {
        final var order = event.getOrder();
        final var createdAt = event.getCreatedAt();

        return PaymentRequestAvroModel.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setSagaId("")
                .setCustomerId(order.getCustomerId().getValue().toString())
                .setOrderId(order.getId().getValue().toString())
                .setPrice(order.getPrice().getAmount())
                .setCreatedAt(createdAt.toInstant())
                .setPaymentOrderStatus(PaymentOrderStatus.CANCELLED)
                .build();
    }

    public RestaurantApprovalRequestAvroModel orderPaidEventoToRestaurantApprovalRequestAvroModel(OrderPaidEvent event) {
        final var order = event.getOrder();
        final var createdAt = event.getCreatedAt();

        return RestaurantApprovalRequestAvroModel.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setSagaId("")
                .setOrderId(order.getId().getValue().toString())
                .setRestaurantId(order.getRestaurantId().getValue().toString())
                .setRestaurantOrderStatus(RestaurantOrderStatus.PAID)
                .setPrice(order.getPrice().getAmount())
                .setCreatedAt(createdAt.toInstant())
                .setProducts(order.getItems().stream().map(orderItem ->
                        Product.newBuilder()
                                .setId(orderItem.getId().getValue().toString())
                                .setQuantity(orderItem.getQuantity())
                                .build()).toList()
                )
                .build();
    }

    public PaymentResponse paymentResponseAvroModelToPaymentResponse(PaymentResponseAvroModel avro) {
        return PaymentResponse.builder()
                .id(avro.getId())
                .sagaId(avro.getSagaId())
                .paymentId(avro.getPaymentId())
                .customerId(avro.getCustomerId())
                .orderId(avro.getOrderId())
                .price(avro.getPrice())
                .createAt(avro.getCreatedAt())
                .paymentStatus(PaymentStatus.valueOf(avro.getPaymentStatus().name()))
                .failureMessages(avro.getFailureMessages())
                .build();
    }

    public RestaurantApprovalResponse restaurantApprovalAvroModelToRestaurantApprovalResponse(RestaurantApprovalResponseAvroModel avro) {
        return RestaurantApprovalResponse.builder()
                .id(avro.getId())
                .sagaId(avro.getSagaId())
                .restaurantId(avro.getRestaurantId())
                .orderId(avro.getOrderId())
                .createAt(avro.getCreatedAt())
                .orderApprovalStatus(OrderApprovalStatus.valueOf(avro.getOrderApprovalStatus().name()))
                .failureMessages(avro.getFailureMessages())
                .build();
    }

}
