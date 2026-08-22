package com.ecommerce.order.domain.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Order {
  private Long id;
  private String orderNumber;
  private String userId;
  private List<OrderItem> orderItems;
}
