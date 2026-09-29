package com.ecommerce.inventory.infrastructure.listener;

import com.ecommerce.inventory.application.service.IUpdateInventoryUseCase;
import com.ecommerce.inventory.infrastructure.config.RabbitMQConfig;
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
    private final RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = RabbitMQConfig.INVENTORY_QUEUE_NAME)
    public void handleOrderPlacedEvent(OrderPlacedEvent orderPlacedEvent) {
    orderPlacedEvent
        .orderItems()
        .forEach(
            orderItem -> {
              try {
                updateInventoryUseCase.reduceStock(orderItem.sku(), orderItem.quantity());
                log.info("Reduced stock for SKU: {} by quantity: {}", orderItem.sku(), orderItem.quantity());
              } catch (Exception ex) {
                log.error("Error reducing stock for SKU: {}: {}", orderItem.sku(), ex.getMessage());
                throw new RuntimeException(ex.getMessage());
              }
            });
    }
}
