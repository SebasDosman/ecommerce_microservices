package com.ecommerce.order.infrastructure.listener;

import com.ecommerce.order.application.service.IUpdateOrderUseCase;
import com.ecommerce.order.domain.model.OrderStatus;
import com.ecommerce.order.infrastructure.config.RabbitMQConfig;
import com.ecommerce.order.infrastructure.event.OrderPlacedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class OrderEventsListener {
  private final IUpdateOrderUseCase updateOrderUseCase;

  @RabbitListener(queues = RabbitMQConfig.ORDER_COMPLETED_QUEUE_NAME)
  public void handleOrderCompletedEvent(OrderPlacedEvent orderPlacedEvent) {
    log.info("Order completed: {}", orderPlacedEvent);
    updateOrderUseCase.updateOrderStatus(orderPlacedEvent.orderNumber(), OrderStatus.CONFIRMED);
  }

  @RabbitListener(queues = RabbitMQConfig.ORDER_CANCELLED_QUEUE_NAME)
  public void handleOrderCancelledEvent(OrderPlacedEvent orderPlacedEvent) {
    log.info("Order cancelled: {}", orderPlacedEvent);
    updateOrderUseCase.updateOrderStatus(orderPlacedEvent.orderNumber(), OrderStatus.CANCELLED);
  }
}
