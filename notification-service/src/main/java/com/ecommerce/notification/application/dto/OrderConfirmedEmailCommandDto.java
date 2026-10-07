package com.ecommerce.notification.application.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OrderConfirmedEmailCommandDto {
  private String orderNumber;
  private String email;
  private List<OrderItemEmailDto> orderItems;

  @AllArgsConstructor
  @NoArgsConstructor
  @Data
  @Builder
  public static class OrderItemEmailDto {
    private String sku;
    private String price;
    private Integer quantity;
  }
}
