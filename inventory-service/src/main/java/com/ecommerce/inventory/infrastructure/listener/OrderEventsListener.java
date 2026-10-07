package com.ecommerce.inventory.infrastructure.listener;

import com.ecommerce.inventory.application.service.IGetInventoryUseCase;
import com.ecommerce.inventory.application.service.IUpdateInventoryUseCase;
import com.ecommerce.inventory.infrastructure.config.RabbitMQConfig;
import com.ecommerce.inventory.infrastructure.event.OrderCancelledEvent;
import com.ecommerce.inventory.infrastructure.event.OrderPlacedEvent;
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

  @RabbitListener(queues = RabbitMQConfig.INVENTORY_QUEUE_NAME)
  public void handleOrderPlacedEvent(OrderPlacedEvent orderPlacedEvent) {
    try {
      boolean areAllProductsInStock =
          orderPlacedEvent.orderItems().stream()
              .allMatch(
                  orderItem ->
                      getInventoryUseCase.isInStock(orderItem.sku(), orderItem.quantity()));

      if (!areAllProductsInStock) {
        log.info("Order PlacedEvent not in stock");
        cancelOrder(orderPlacedEvent, "Some products are not in stock");
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
          RabbitMQConfig.ORDER_COMPLETED_ROUTING_KEY,
          orderPlacedEvent);
    } catch (Exception e) {
      log.error("Error occurred while processing order placed event", e);
      cancelOrder(orderPlacedEvent, "Error occurred while processing order placed event");
    }
  }

  private void cancelOrder(OrderPlacedEvent orderPlacedEvent, String reason) {
    OrderCancelledEvent orderCancelledEvent =
        OrderCancelledEvent.builder()
            .orderNumber(orderPlacedEvent.orderNumber())
            .email(orderPlacedEvent.email())
            .cancelReason(reason)
            .build();

    rabbitTemplate.convertAndSend(
        RabbitMQConfig.ORDER_EXCHANGE_NAME,
        RabbitMQConfig.ORDER_CANCELLED_ROUTING_KEY,
        orderCancelledEvent);
  }
}
