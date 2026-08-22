package com.ecommerce.inventory.application.service;

import com.ecommerce.inventory.application.dto.InventoryRequestDto;
import com.ecommerce.inventory.application.dto.InventoryResponseDto;

public interface IUpdateInventoryUseCase {
  InventoryResponseDto update(Long id, InventoryRequestDto inventoryRequestDto);
  void reduceStock(String sku, Integer quantity);
}
