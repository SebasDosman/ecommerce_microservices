package com.ecommerce.order.domain.model;

import java.util.List;

import com.ecommerce.order.infrastructure.persistence.OrderStatus;
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
  private OrderStatus status;
}
