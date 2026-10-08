package com.ecommerce.notification.infrastructure.listener;

import com.ecommerce.notification.application.mapper.OrderEventToEmailCommandMapper;
import com.ecommerce.notification.application.service.IOrderEmailService;
import com.ecommerce.notification.infrastructure.config.RabbitMQConfig;
import com.ecommerce.notification.infrastructure.event.OrderEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderEventsListener {
  private final IOrderEmailService orderEmailService;
  private final OrderEventToEmailCommandMapper eventMapper;

  @RabbitListener(queues = RabbitMQConfig.NOTIFICATION_ORDER_EVENTS_QUEUE_NAME)
  public void handleOrderEvent(OrderEvent event) {
    log.info(
        "Received order {} event with type {} and email {}",
        event.orderNumber(),
        event.type(),
        event.email());
    switch (event.type()) {
      case OrderEvent.PLACED ->
          orderEmailService.sendOrderPlaced(eventMapper.mapOrderPlacedEventToEmailCommand(event));
      case OrderEvent.CONFIRMED ->
          orderEmailService.sendOrderConfirmed(
              eventMapper.mapOrderConfirmedEventToEmailCommand(event));
      case OrderEvent.CANCELLED ->
          orderEmailService.sendOrderCancelled(
              eventMapper.mapOrderCancelledEventToEmailCommand(event));
      default -> log.warn("Ignoring unsupported order event type: {}", event.type());
    }
  }
}
