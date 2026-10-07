package com.ecommerce.notification.infrastructure.listener;

import com.ecommerce.notification.application.mapper.OrderEventToEmailCommandMapper;
import com.ecommerce.notification.application.service.IOrderEmailService;
import com.ecommerce.notification.infrastructure.config.RabbitMQConfig;
import com.ecommerce.notification.infrastructure.event.OrderCancelledEvent;
import com.ecommerce.notification.infrastructure.event.OrderConfirmedEvent;
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
  public void handleOrderPlacedEvent(OrderPlacedEvent orderPlacedEvent) {
    try {
      log.info(
          "Received order placed event for order {} and email {}",
          orderPlacedEvent.orderNumber(),
          orderPlacedEvent.email());
      var orderEmailCommand = eventMapper.mapOrderPlacedEventToEmailCommand(orderPlacedEvent);
      orderEmailService.sendOrderPlaced(orderEmailCommand);
    } catch (Exception ex) {
      log.error(
          "Error processing order placed event for order {}",
          orderPlacedEvent.orderNumber(),
          ex);
      throw ex;
    }
  }

  @RabbitListener(queues = "order-confirmed-queue")
  public void handleOrderConfirmedEvent(OrderConfirmedEvent orderConfirmedEvent) {
    try {
      log.info(
          "Received order confirmed event for order {} and email {}",
          orderConfirmedEvent.orderNumber(),
          orderConfirmedEvent.email());
      var orderEmailCommand = eventMapper.mapOrderConfirmedEventToEmailCommand(orderConfirmedEvent);
      orderEmailService.sendOrderConfirmed(orderEmailCommand);
    } catch (Exception ex) {
      log.error(
          "Error processing order confirmed event for order {}",
          orderConfirmedEvent.orderNumber(),
          ex);
      throw ex;
    }
  }

  @RabbitListener(queues = "order-cancelled-queue")
  public void handleOrderCancelledEvent(OrderCancelledEvent orderCancelledEvent) {
    try {
      log.info(
          "Received order cancelled event for order {} and email {}",
          orderCancelledEvent.orderNumber(),
          orderCancelledEvent.email());
      var orderEmailCommand = eventMapper.mapOrderCancelledEventToEmailCommand(orderCancelledEvent);
      orderEmailService.sendOrderCancelled(orderEmailCommand);
    } catch (Exception ex) {
      log.error(
          "Error processing order cancelled event for order {}",
          orderCancelledEvent.orderNumber(),
          ex);
      throw ex;
    }
  }
}
