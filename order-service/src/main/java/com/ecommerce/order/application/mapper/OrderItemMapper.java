package com.ecommerce.order.application.mapper;

import com.ecommerce.order.application.dto.OrderItemRequestDto;
import com.ecommerce.order.domain.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {
    @Mapping(target = "id", ignore = true)
    OrderItem toOrderItem(OrderItemRequestDto orderItemRequestDto);
}
