package com.ecommerce.notification.infrastructure.event;

import java.util.List;

public record OrderPlacedEvent(String orderNumber, String email, List<OrderItemEvent> orderItems) {
  public record OrderItemEvent(String sku, String price, Integer quantity) {}
}
