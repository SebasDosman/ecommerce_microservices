package com.ecommerce.order.application.dto;

import java.util.List;

import com.ecommerce.order.domain.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OrderResponseDto {
  private Long id;
  private String orderNumber;
  private List<OrderItemResponseDto> orderItems;
  private OrderStatus status;
}
