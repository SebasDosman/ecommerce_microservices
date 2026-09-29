package com.ecommerce.notification.application.service;

import com.ecommerce.notification.infrastructure.event.OrderPlacedEvent;

public interface IOrderEmailService {
  void sendOrderConfirmation(OrderPlacedEvent orderPlacedEvent);
}
