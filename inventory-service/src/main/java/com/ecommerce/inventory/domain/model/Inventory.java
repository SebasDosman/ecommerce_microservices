package com.ecommerce.inventory.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Inventory {
  private Long id;
  private String sku;
  private Integer quantity;

  public boolean isInStock() {
    return this.quantity > 0;
  }
}
