package com.ecommerce.inventory.application.service.implementation;

import com.ecommerce.inventory.application.dto.InventoryResponseDto;
import com.ecommerce.inventory.application.mapper.InventoryMapper;
import com.ecommerce.inventory.application.service.IGetInventoryUseCase;
import com.ecommerce.inventory.application.validator.InventoryValidator;
import com.ecommerce.inventory.domain.repository.InventoryRepository;
import com.ecommerce.inventory.shared.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@RefreshScope
public class GetInventoryUseCase implements IGetInventoryUseCase {
  private final InventoryRepository inventoryRepository;
  private final InventoryMapper inventoryMapper;

  @Value("${ecommerce.services.inventory.allow-backorders}")
  private boolean allowBackorder;

  @Override
  @Transactional(readOnly = true)
  public Slice<InventoryResponseDto> getAll(Pageable pageable) {
    log.info(
        "Fetching all inventories with pagination: page number = {}, page size = {}",
        pageable.getPageNumber(),
        pageable.getPageSize());

    return inventoryRepository.findAll(pageable).map(inventoryMapper::toInventoryResponseDto);
  }

  @Override
  @Transactional(readOnly = true)
  public InventoryResponseDto getById(Long id) {
    log.info("Fetching inventory with id: {}", id);

    return inventoryMapper.toInventoryResponseDto(
        inventoryRepository
            .findById(id)
            .orElseThrow(() ->
                new NotFoundException(
                    String.format(InventoryValidator.INVENTORY_NOT_FOUND, id))));
  }

  @Override
  @Transactional(readOnly = true)
  public boolean isInStock(String sku, Integer quantity) {
    log.info("Checking stock availability for SKU: {} with requested quantity: {}", sku, quantity);

    if (allowBackorder) {
      log.warn(
          "Backorders are allowed. Returning true for stock availability check for SKU: {}", sku);
      return true;
    }

    return inventoryRepository
        .findBySku(sku)
        .map(inventory -> inventory.getQuantity() >= quantity)
        .orElse(false);
  }
}
