package com.ecommerce.order.application.service;

import com.ecommerce.order.application.dto.OrderResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface IGetOrderUseCase {
  Slice<OrderResponseDto> getAll(Pageable pageable);
  OrderResponseDto getById(Long id);
  Slice<OrderResponseDto> getByUserId(Pageable pageable, String userId);
}
