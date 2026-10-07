package com.ecommerce.notification.application.service;

import com.ecommerce.notification.application.dto.OrderEmailCommandDto;

public interface IOrderEmailService {
  void sendOrderConfirmation(OrderEmailCommandDto orderEmailCommand);
}
