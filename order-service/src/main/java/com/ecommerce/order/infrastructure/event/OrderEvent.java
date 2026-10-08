package com.ecommerce.order.infrastructure.event;

import java.util.List;

public record OrderEvent(
    String type,
    String orderNumber,
    String email,
    List<OrderItemEvent> orderItems,
    String cancelReason) {

  public static final String PLACED = "PLACED";
  public static final String CONFIRMED = "CONFIRMED";
  public static final String CANCELLED = "CANCELLED";

  public record OrderItemEvent(String sku, String price, Integer quantity) {}
}
