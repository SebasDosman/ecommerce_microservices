package com.ecommerce.order.infrastructure.event;

import com.ecommerce.order.domain.event.DomainEventPublisher;
import com.ecommerce.order.domain.model.Order;
import com.ecommerce.order.infrastructure.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RabbitDomainEventPublisher implements DomainEventPublisher {
  private final RabbitTemplate rabbitTemplate;

  @Override
  public void publishOrderPlaced(Order order, String email) {
    var orderItemEvents = order.getOrderItems().stream()
        .map(item -> new OrderPlacedEvent.OrderItemEvent(
            item.getSku(),
            item.getPrice() != null ? item.getPrice().toPlainString() : "0.00",
            item.getQuantity()))
        .toList();

    var event = new OrderPlacedEvent(order.getOrderNumber(), email, orderItemEvents);
    
    rabbitTemplate.convertAndSend(
        RabbitMQConfig.ORDER_EXCHANGE_NAME,
        RabbitMQConfig.ORDER_ROUTING_KEY,
        event);

    log.info("Published order placed event for order: {}", order.getOrderNumber());
  }
}
