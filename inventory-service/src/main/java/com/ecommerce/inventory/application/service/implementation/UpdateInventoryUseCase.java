package com.ecommerce.inventory.application.service.implementation;

import com.ecommerce.inventory.application.dto.InventoryRequestDto;
import com.ecommerce.inventory.application.dto.InventoryResponseDto;
import com.ecommerce.inventory.application.mapper.InventoryMapper;
import com.ecommerce.inventory.application.service.IUpdateInventoryUseCase;
import com.ecommerce.inventory.application.validator.InventoryValidator;
import com.ecommerce.inventory.domain.model.Inventory;
import com.ecommerce.inventory.domain.repository.InventoryRepository;
import com.ecommerce.inventory.shared.exception.ConflictException;
import com.ecommerce.inventory.shared.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateInventoryUseCase implements IUpdateInventoryUseCase {
  private final InventoryRepository inventoryRepository;
  private final InventoryMapper inventoryMapper;

  @Override
  @Transactional
  public InventoryResponseDto update(Long id, InventoryRequestDto inventoryRequestDto) {
    Inventory inventory = inventoryRepository.findById(id)
        .orElseThrow(() -> new NotFoundException(String.format(InventoryValidator.INVENTORY_NOT_FOUND, id)));

    inventoryMapper.updateInventory(inventoryRequestDto, inventory);
    log.info("Updating inventory with id {}", id);

    return inventoryMapper.toInventoryResponseDto(inventoryRepository.save(inventory));
  }

  @Override
  @Transactional
  public void reduceStock(String sku, Integer quantity) {
    log.info("Reducing stock for SKU: {} by quantity: {}", sku, quantity);

    Inventory inventory = inventoryRepository
            .findBySku(sku)
            .orElseThrow(() ->
                    new NotFoundException(
                            String.format(InventoryValidator.INVENTORY_SKU_NOT_FOUND, sku)));

    if (inventory.getQuantity() < quantity) {
      throw new ConflictException(
              String.format(InventoryValidator.INSUFFICIENT_STOCK, sku, inventory.getQuantity(), quantity));
    }

    inventory.setQuantity(inventory.getQuantity() - quantity);
    inventoryRepository.save(inventory);
  }
}
