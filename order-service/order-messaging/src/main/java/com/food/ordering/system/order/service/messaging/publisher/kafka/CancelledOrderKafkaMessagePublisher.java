package com.food.ordering.system.order.service.messaging.publisher.kafka;

import com.food.ordering.system.kafka.order.avro.model.PaymentRequestAvroModel;
import com.food.ordering.system.kafka.producer.KafkaMessageHelper;
import com.food.ordering.system.kafka.producer.service.KafkaProducer;
import com.food.ordering.system.order.service.domain.config.OrderServiceConfigData;
import com.food.ordering.system.order.service.domain.event.OrderCancelledEvent;
import com.food.ordering.system.order.service.domain.ports.output.message.publisher.payment.OrderCancelledPaymentRequestMessagePublisher;
import com.food.ordering.system.order.service.messaging.mapper.OrderMessagingDataMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CancelledOrderKafkaMessagePublisher implements OrderCancelledPaymentRequestMessagePublisher {

    private final OrderMessagingDataMapper orderMessagingDataMapper;
    private final OrderServiceConfigData orderServiceConfigData;
    private final KafkaProducer<String, PaymentRequestAvroModel> kafkaProducer;
    private final KafkaMessageHelper kafkaMessageHelper;

    public CancelledOrderKafkaMessagePublisher(final OrderMessagingDataMapper orderMessagingDataMapper,
                                               final OrderServiceConfigData orderServiceConfigData,
                                               final KafkaProducer<String, PaymentRequestAvroModel> kafkaProducer,
                                               final KafkaMessageHelper kafkaMessageHelper) {
        this.orderMessagingDataMapper = orderMessagingDataMapper;
        this.orderServiceConfigData = orderServiceConfigData;
        this.kafkaProducer = kafkaProducer;
        this.kafkaMessageHelper = kafkaMessageHelper;
    }


    @Override
    public void publish(OrderCancelledEvent event) {
        final var orderId = event.getOrder().getId().getValue().toString();
        log.info("Received OrderCancelledEvent for order id: {}", orderId);

        try {

            final var paymentRequestAvroModel = this.orderMessagingDataMapper.orderCancelledEventToPaymentRequestAvroModel(event);

            this.kafkaProducer.send(this.orderServiceConfigData.getPaymentRequestTopicName(),
                    orderId,
                    paymentRequestAvroModel,
                    this.kafkaMessageHelper.getKafkaCallback(this.orderServiceConfigData.getPaymentRequestTopicName(),
                            paymentRequestAvroModel,
                            orderId,
                            paymentRequestAvroModel.getClass().getSimpleName()));

            log.info("PaymentRequestAvroModel sent to Kafka for order id: {}", orderId);

        } catch (Exception e) {
            log.error("Error while sending PaymentRequestAvroModel message to kafka with order id: {}, error: {}", orderId, e.getMessage());
        }


    }
}
