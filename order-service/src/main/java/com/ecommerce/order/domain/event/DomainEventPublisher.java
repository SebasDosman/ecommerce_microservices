package com.ecommerce.order.domain.event;

import com.ecommerce.order.domain.model.Order;

public interface DomainEventPublisher {
  void publishOrderPlaced(Order order, String email);
}
