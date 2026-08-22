package com.ecommerce.inventory.domain.repository;

import com.ecommerce.inventory.domain.model.Inventory;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface InventoryRepository {
  Slice<Inventory> findAll(Pageable pageable);
  Optional<Inventory> findById(Long id);
  Inventory save(Inventory inventory);
  void deleteById(Long id);
  boolean existsById(Long id);
  boolean existsBySku(String sku);
  Optional<Inventory> findBySku(String sku);
}
