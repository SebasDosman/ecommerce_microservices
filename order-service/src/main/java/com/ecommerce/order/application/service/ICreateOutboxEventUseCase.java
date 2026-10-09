package com.ecommerce.order.application.service;

import com.ecommerce.order.domain.model.Order;

public interface ICreateOutboxEventUseCase {
  void create(Order order, String email);
}
