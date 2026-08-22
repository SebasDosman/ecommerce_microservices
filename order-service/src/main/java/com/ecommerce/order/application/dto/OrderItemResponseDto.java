package com.ecommerce.order.application.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OrderItemResponseDto {
  private Long id;
  private String sku;
  private BigDecimal price;
  private Integer quantity;
}
