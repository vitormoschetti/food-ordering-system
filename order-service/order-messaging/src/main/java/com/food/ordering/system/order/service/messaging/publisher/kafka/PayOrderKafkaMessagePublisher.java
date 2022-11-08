package com.food.ordering.system.order.service.messaging.publisher.kafka;

import com.food.ordering.system.kafka.order.avro.model.RestaurantApprovalRequestAvroModel;
import com.food.ordering.system.kafka.producer.KafkaMessageHelper;
import com.food.ordering.system.kafka.producer.service.KafkaProducer;
import com.food.ordering.system.order.service.domain.config.OrderServiceConfigData;
import com.food.ordering.system.order.service.domain.event.OrderPaidEvent;
import com.food.ordering.system.order.service.domain.ports.output.message.publisher.restaurantapproval.OrderPaidRestaurantRequestMessagePublisher;
import com.food.ordering.system.order.service.messaging.mapper.OrderMessagingDataMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PayOrderKafkaMessagePublisher implements OrderPaidRestaurantRequestMessagePublisher {

    private final OrderMessagingDataMapper orderMessagingDataMapper;
    private final OrderServiceConfigData orderServiceConfigData;
    private final KafkaProducer<String, RestaurantApprovalRequestAvroModel> kafkaProducer;
    private final KafkaMessageHelper kafkaMessageHelper;

    public PayOrderKafkaMessagePublisher(final OrderMessagingDataMapper orderMessagingDataMapper,
                                         final OrderServiceConfigData orderServiceConfigData,
                                         final KafkaProducer<String, RestaurantApprovalRequestAvroModel> kafkaProducer,
                                         final KafkaMessageHelper kafkaMessageHelper) {
        this.orderMessagingDataMapper = orderMessagingDataMapper;
        this.orderServiceConfigData = orderServiceConfigData;
        this.kafkaProducer = kafkaProducer;
        this.kafkaMessageHelper = kafkaMessageHelper;
    }


    @Override
    public void publish(OrderPaidEvent event) {
        final var orderId = event.getOrder().getId().getValue().toString();
        log.info("Received OrderPaidEvent for order id: {}", orderId);

        try {

            final var restaurantApprovalRequestAvroModel = this.orderMessagingDataMapper.orderPaidEventoToRestaurantApprovalRequestAvroModel(event);

            this.kafkaProducer.send(this.orderServiceConfigData.getPaymentRequestTopicName(),
                    orderId,
                    restaurantApprovalRequestAvroModel,
                    this.kafkaMessageHelper.getKafkaCallback(this.orderServiceConfigData.getPaymentRequestTopicName(),
                            restaurantApprovalRequestAvroModel,
                            orderId,
                            restaurantApprovalRequestAvroModel.getClass().getSimpleName()));

            log.info("RestaurantApprovalRequestAvroModel sent to Kafka for order id: {}", orderId);

        } catch (Exception e) {
            log.error("Error while sending RestaurantApprovalRequestAvroModel message to kafka with order id: {}, error: {}", orderId, e.getMessage());
        }


    }
}
