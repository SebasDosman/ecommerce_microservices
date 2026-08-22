package com.ecommerce.inventory.application.service.implementation;

import com.ecommerce.inventory.application.dto.InventoryRequestDto;
import com.ecommerce.inventory.application.dto.InventoryResponseDto;
import com.ecommerce.inventory.application.mapper.InventoryMapper;
import com.ecommerce.inventory.application.service.ICreateInventoryUseCase;
import com.ecommerce.inventory.application.validator.InventoryValidator;
import com.ecommerce.inventory.domain.model.Inventory;
import com.ecommerce.inventory.domain.repository.InventoryRepository;
import com.ecommerce.inventory.shared.exception.ConflictException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreateInventoryUseCase implements ICreateInventoryUseCase {
  private final InventoryRepository inventoryRepository;
  private final InventoryMapper inventoryMapper;

  @Override
  @Transactional
  public InventoryResponseDto create(InventoryRequestDto inventoryRequestDto) {
    if (inventoryRepository.existsBySku(inventoryRequestDto.getSku()))
      throw new ConflictException(
          String.format(InventoryValidator.INVENTORY_ALREADY_EXISTS, inventoryRequestDto.getSku()));

    Inventory inventory = inventoryMapper.toInventory(inventoryRequestDto);

    log.info("Creating inventory for SKU: {}", inventory.getSku());

    return inventoryMapper.toInventoryResponseDto(inventoryRepository.save(inventory));
  }
}
