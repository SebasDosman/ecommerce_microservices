package com.ecommerce.order.infrastructure.listener;

import com.ecommerce.order.application.service.IUpdateOrderUseCase;
import com.ecommerce.order.domain.model.OrderStatus;
import com.ecommerce.order.infrastructure.config.RabbitMQConfig;
import com.ecommerce.order.infrastructure.event.OrderEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class OrderEventsListener {
  private final IUpdateOrderUseCase updateOrderUseCase;

  @RabbitListener(queues = RabbitMQConfig.ORDER_STATUS_EVENTS_QUEUE_NAME)
  public void handleOrderStatusEvent(OrderEvent event) {
    log.info("Order status event received: {}", event);

    switch (event.type()) {
      case OrderEvent.CONFIRMED ->
          updateOrderUseCase.updateOrderStatus(event.orderNumber(), OrderStatus.CONFIRMED);
      case OrderEvent.CANCELLED ->
          updateOrderUseCase.updateOrderStatus(event.orderNumber(), OrderStatus.CANCELLED);
      default -> log.warn("Ignoring unsupported order status event type: {}", event.type());
    }
  }
}
