package com.ecommerce.inventory.infrastructure.persistence;

import com.ecommerce.inventory.domain.model.Inventory;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface InventoryEntityMapper {
  Inventory toDomainModel(InventoryEntity entity);

  InventoryEntity toEntity(Inventory inventory);

  void updateEntity(Inventory inventory, @MappingTarget InventoryEntity entity);
}
