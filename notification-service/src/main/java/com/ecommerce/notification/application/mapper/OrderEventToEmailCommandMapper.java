package com.ecommerce.notification.application.mapper;

import com.ecommerce.notification.application.dto.OrderEmailCommandDto;
import com.ecommerce.notification.infrastructure.event.OrderPlacedEvent;
import org.springframework.stereotype.Component;

@Component
public class OrderEventToEmailCommandMapper {
  public OrderEmailCommandDto mapOrderPlacedEventToEmailCommand(OrderPlacedEvent event) {
    return OrderEmailCommandDto.builder()
        .orderNumber(event.orderNumber())
        .email(event.email())
        .orderItems(
            event.orderItems().stream()
                .map(
                    item ->
                        OrderEmailCommandDto.OrderItemEmailDto.builder()
                            .sku(item.sku())
                            .price(item.price())
                            .quantity(item.quantity())
                            .build())
                .toList())
        .build();
  }
}
