package com.ecommerce.order.infrastructure.persistence;

import com.ecommerce.order.domain.model.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderEntityMapper {
    Order toDomainModel(OrderEntity entity);

    OrderEntity toEntity(Order order);
}
