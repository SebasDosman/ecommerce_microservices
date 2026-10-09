package com.ecommerce.order.infrastructure.persistence;

import com.ecommerce.order.domain.model.OutboxEvent;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OutboxEventEntityMapper {
    OutboxEvent toDomainModel(OutboxEventEntity entity);
    OutboxEventEntity toEntity(OutboxEvent domain);
}
