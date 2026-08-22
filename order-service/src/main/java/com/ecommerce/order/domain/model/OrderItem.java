package com.ecommerce.order.domain.model;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class OrderItem {
  private Long id;
  private String sku;
  private BigDecimal price;
  private Integer quantity;
}
