package com.ecommerce.order.application.service;

import com.ecommerce.order.application.dto.OrderResponseDto;
import com.ecommerce.order.domain.model.OrderStatus;

public interface IUpdateOrderUseCase {
  OrderResponseDto updateOrderStatus(String orderNumber, OrderStatus orderStatus);
}
