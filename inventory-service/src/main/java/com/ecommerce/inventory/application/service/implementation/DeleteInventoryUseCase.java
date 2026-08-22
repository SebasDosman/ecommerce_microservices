package com.ecommerce.inventory.application.service.implementation;

import com.ecommerce.inventory.application.service.IDeleteInventoryUseCase;
import com.ecommerce.inventory.application.validator.InventoryValidator;
import com.ecommerce.inventory.domain.repository.InventoryRepository;
import com.ecommerce.inventory.shared.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeleteInventoryUseCase implements IDeleteInventoryUseCase {
  private final InventoryRepository inventoryRepository;

  @Override
  @Transactional
  public void delete(Long id) {
    if (!inventoryRepository.existsById(id))
      throw new NotFoundException(String.format(InventoryValidator.INVENTORY_NOT_FOUND, id));

    log.info("Deleting inventory with id: {}", id);

    inventoryRepository.deleteById(id);
  }
}
