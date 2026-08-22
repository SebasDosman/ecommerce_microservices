package com.ecommerce.inventory.infrastructure.persistence;

import com.ecommerce.inventory.domain.model.Inventory;
import com.ecommerce.inventory.domain.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class InventoryRepositoryAdapter implements InventoryRepository {
  private final InventoryEntityRepository inventoryEntityRepository;
  private final InventoryEntityMapper inventoryEntityMapper;

  @Override
  public Slice<Inventory> findAll(Pageable pageable) {
    return inventoryEntityRepository.findAll(pageable).map(inventoryEntityMapper::toDomainModel);
  }

  @Override
  public Optional<Inventory> findById(Long id) {
    return inventoryEntityRepository.findById(id).map(inventoryEntityMapper::toDomainModel);
  }

  @Override
  public Inventory save(Inventory inventory) {
    InventoryEntity entity = inventoryEntityMapper.toEntity(inventory);
    InventoryEntity saved = inventoryEntityRepository.save(entity);
    return inventoryEntityMapper.toDomainModel(saved);
  }

  @Override
  public void deleteById(Long id) {
    inventoryEntityRepository.deleteById(id);
  }

  @Override
  public boolean existsById(Long id) {
    return inventoryEntityRepository.existsById(id);
  }

  @Override
  public boolean existsBySku(String sku) {
    return inventoryEntityRepository.existsBySku(sku);
  }

  @Override
  public Optional<Inventory> findBySku(String sku) {
    return inventoryEntityRepository.findBySku(sku).map(inventoryEntityMapper::toDomainModel);
  }
}
