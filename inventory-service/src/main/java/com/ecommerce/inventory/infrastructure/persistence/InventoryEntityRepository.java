package com.ecommerce.inventory.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface InventoryEntityRepository extends JpaRepository<InventoryEntity, Long> {
  boolean existsBySku(String sku);
  Optional<InventoryEntity> findBySku(String sku);
}
