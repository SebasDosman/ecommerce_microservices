package com.ecommerce.inventory.infrastructure.listener;

import com.ecommerce.inventory.application.service.IGetInventoryUseCase;
import com.ecommerce.inventory.application.service.IUpdateInventoryUseCase;
import com.ecommerce.inventory.infrastructure.config.RabbitMQConfig;
import com.ecommerce.inventory.infrastructure.event.OrderEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class OrderEventsListener {
  private final IUpdateInventoryUseCase updateInventoryUseCase;
  private final IGetInventoryUseCase getInventoryUseCase;
  private final RabbitTemplate rabbitTemplate;

  @RabbitListener(queues = RabbitMQConfig.INVENTORY_ORDER_EVENTS_QUEUE_NAME)
  public void handleOrderPlacedEvent(OrderEvent orderPlacedEvent) {
    try {
      boolean areAllProductsInStock =
          orderPlacedEvent.orderItems().stream()
              .allMatch(
                  orderItem ->
                      getInventoryUseCase.isInStock(orderItem.sku(), orderItem.quantity()));

      if (!areAllProductsInStock) {
        log.info("Order PlacedEvent not in stock");
        cancelOrder(orderPlacedEvent, "Some products are not in stock");
        return;
      }

      orderPlacedEvent
          .orderItems()
          .forEach(
              orderItem -> {
                updateInventoryUseCase.reduceStock(orderItem.sku(), orderItem.quantity());
                log.info(
                    "Reduced stock for SKU: {} by quantity: {}",
                    orderItem.sku(),
                    orderItem.quantity());
              });
      rabbitTemplate.convertAndSend(
          RabbitMQConfig.ORDER_EXCHANGE_NAME,
          RabbitMQConfig.ORDER_CONFIRMED_ROUTING_KEY,
          new OrderEvent(
              OrderEvent.CONFIRMED,
              orderPlacedEvent.orderNumber(),
              orderPlacedEvent.email(),
              null,
              null));
    } catch (Exception e) {
      log.error("Error occurred while processing order placed event", e);
      cancelOrder(orderPlacedEvent, "Error occurred while processing order placed event");
    }
  }

  private void cancelOrder(OrderEvent orderPlacedEvent, String reason) {
    OrderEvent orderCancelledEvent =
        new OrderEvent(
            OrderEvent.CANCELLED,
            orderPlacedEvent.orderNumber(),
            orderPlacedEvent.email(),
            null,
            reason);

    rabbitTemplate.convertAndSend(
        RabbitMQConfig.ORDER_EXCHANGE_NAME,
        RabbitMQConfig.ORDER_CANCELLED_ROUTING_KEY,
        orderCancelledEvent);
  }
}
