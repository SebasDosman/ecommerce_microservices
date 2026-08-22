package com.ecommerce.inventory.application.mapper;

import com.ecommerce.inventory.application.dto.InventoryRequestDto;
import com.ecommerce.inventory.application.dto.InventoryResponseDto;
import com.ecommerce.inventory.domain.model.Inventory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface InventoryMapper {
  @Mapping(target = "inStock", expression = "java(inventory.getQuantity() > 0)")
  InventoryResponseDto toInventoryResponseDto(Inventory inventory);

  @Mapping(target = "id", ignore = true)
  Inventory toInventory(InventoryRequestDto inventoryRequestDto);

  @Mapping(target = "id", ignore = true)
  void updateInventory(InventoryRequestDto inventoryRequestDto, @MappingTarget Inventory inventory);
}
