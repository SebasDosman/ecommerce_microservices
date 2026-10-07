package com.ecommerce.notification.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OrderCancelledEmailCommandDto {
  private String orderNumber;
  private String email;
  private String cancelReason;
}
