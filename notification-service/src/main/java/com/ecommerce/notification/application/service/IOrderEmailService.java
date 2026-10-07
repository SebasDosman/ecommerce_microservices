package com.ecommerce.notification.application.service;

import com.ecommerce.notification.application.dto.OrderCancelledEmailCommandDto;
import com.ecommerce.notification.application.dto.OrderConfirmedEmailCommandDto;
import com.ecommerce.notification.application.dto.OrderEmailCommandDto;

public interface IOrderEmailService {
  void sendOrderPlaced(OrderEmailCommandDto orderEmailCommand);

  void sendOrderConfirmed(OrderConfirmedEmailCommandDto orderEmailCommand);

  void sendOrderCancelled(OrderCancelledEmailCommandDto orderEmailCommand);
}
