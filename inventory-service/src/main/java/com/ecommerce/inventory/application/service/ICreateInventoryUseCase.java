package com.ecommerce.inventory.application.service;

import com.ecommerce.inventory.application.dto.InventoryRequestDto;
import com.ecommerce.inventory.application.dto.InventoryResponseDto;

public interface ICreateInventoryUseCase {
  InventoryResponseDto create(InventoryRequestDto inventoryRequestDto);
}
