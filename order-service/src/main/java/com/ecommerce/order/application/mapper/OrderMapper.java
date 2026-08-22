package com.ecommerce.order.application.mapper;

import com.ecommerce.order.application.dto.OrderResponseDto;
import com.ecommerce.order.domain.model.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {
  OrderResponseDto toOrderResponseDto(Order order);
}
