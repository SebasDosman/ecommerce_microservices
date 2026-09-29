package com.ecommerce.inventory.infrastructure.event;

import java.util.List;

public record OrderPlacedEvent(String orderNumber, String email, List<OrderItemEvent> orderItems) {
  public record OrderItemEvent(String sku, String price, Integer quantity) {}
}
