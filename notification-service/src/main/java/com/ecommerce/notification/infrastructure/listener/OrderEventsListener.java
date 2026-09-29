package com.ecommerce.notification.infrastructure.listener;

import com.ecommerce.notification.application.service.IOrderEmailService;
import com.ecommerce.notification.infrastructure.config.RabbitMQConfig;
import com.ecommerce.notification.infrastructure.event.OrderPlacedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderEventsListener {
  private final IOrderEmailService orderEmailService;

  @RabbitListener(queues = RabbitMQConfig.NOTIFICATION_QUEUE_NAME)
  public void handleOrderPlacedEvent(OrderPlacedEvent orderPlacedEvent) {
    try {
      log.info(
          "Received order placed event for order {} and email {}",
          orderPlacedEvent.orderNumber(),
          orderPlacedEvent.email());
      orderEmailService.sendOrderConfirmation(orderPlacedEvent);
    } catch (Exception ex) {
      log.error(
          "Error processing order placed event for order {}",
          orderPlacedEvent.orderNumber(),
          ex);
      throw ex;
    }
  }
}
