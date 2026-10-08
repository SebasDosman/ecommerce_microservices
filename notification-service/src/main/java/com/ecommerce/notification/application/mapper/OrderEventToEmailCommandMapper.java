package com.ecommerce.notification.application.mapper;

import com.ecommerce.notification.application.dto.OrderCancelledEmailCommandDto;
import com.ecommerce.notification.application.dto.OrderConfirmedEmailCommandDto;
import com.ecommerce.notification.application.dto.OrderEmailCommandDto;
import com.ecommerce.notification.infrastructure.event.OrderEvent;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class OrderEventToEmailCommandMapper {

  public OrderEmailCommandDto mapOrderPlacedEventToEmailCommand(OrderEvent orderPlacedEvent) {
    if (orderPlacedEvent == null) {
      return null;
    }

    List<OrderEmailCommandDto.OrderItemEmailDto> orderItems = Collections.emptyList();
    if (orderPlacedEvent.orderItems() != null) {
      orderItems =
          orderPlacedEvent.orderItems().stream()
              .map(
                  item ->
                      OrderEmailCommandDto.OrderItemEmailDto.builder()
                          .sku(item.sku())
                          .price(item.price())
                          .quantity(item.quantity())
                          .build())
              .collect(Collectors.toList());
    }

    return OrderEmailCommandDto.builder()
        .orderNumber(orderPlacedEvent.orderNumber())
        .email(orderPlacedEvent.email())
        .orderItems(orderItems)
        .build();
  }

  public OrderConfirmedEmailCommandDto mapOrderConfirmedEventToEmailCommand(
      OrderEvent orderConfirmedEvent) {
    if (orderConfirmedEvent == null) {
      return null;
    }

    return OrderConfirmedEmailCommandDto.builder()
        .orderNumber(orderConfirmedEvent.orderNumber())
        .email(orderConfirmedEvent.email())
        .orderItems(Collections.emptyList())
        .build();
  }

  public OrderCancelledEmailCommandDto mapOrderCancelledEventToEmailCommand(
      OrderEvent orderCancelledEvent) {
    if (orderCancelledEvent == null) {
      return null;
    }

    return OrderCancelledEmailCommandDto.builder()
        .orderNumber(orderCancelledEvent.orderNumber())
        .email(orderCancelledEvent.email())
        .cancelReason(orderCancelledEvent.cancelReason())
        .build();
  }
}
