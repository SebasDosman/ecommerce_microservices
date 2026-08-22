package com.ecommerce.order.application.service;

import com.ecommerce.order.application.dto.OrderRequestDto;
import com.ecommerce.order.application.dto.OrderResponseDto;

public interface ICreateOrderUseCase {
  OrderResponseDto create(OrderRequestDto orderRequestDto, String userId);
}
