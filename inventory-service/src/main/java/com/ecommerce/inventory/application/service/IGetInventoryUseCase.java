package com.ecommerce.inventory.application.service;

import com.ecommerce.inventory.application.dto.InventoryResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface IGetInventoryUseCase {
  Slice<InventoryResponseDto> getAll(Pageable pageable);
  InventoryResponseDto getById(Long id);
  boolean isInStock(String sku, Integer quantity);
}
