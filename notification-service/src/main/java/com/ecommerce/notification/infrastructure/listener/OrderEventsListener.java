package com.ecommerce.notification.infrastructure.listener;

import com.ecommerce.notification.application.mapper.OrderEventToEmailCommandMapper;
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
  private final OrderEventToEmailCommandMapper eventMapper;

  @RabbitListener(queues = RabbitMQConfig.NOTIFICATION_QUEUE_NAME)
  public void handleOrderCompletedEvent(OrderPlacedEvent orderPlacedEvent) {
    try {
      log.info(
          "Received order completed event for order {} and email {}",
          orderPlacedEvent.orderNumber(),
          orderPlacedEvent.email());
      var orderEmailCommand = eventMapper.mapOrderPlacedEventToEmailCommand(orderPlacedEvent);
      orderEmailService.sendOrderConfirmation(orderEmailCommand);
    } catch (Exception ex) {
      log.error(
          "Error processing order completed event for order {}",
          orderPlacedEvent.orderNumber(),
          ex);
      throw ex;
    }
  }
}
