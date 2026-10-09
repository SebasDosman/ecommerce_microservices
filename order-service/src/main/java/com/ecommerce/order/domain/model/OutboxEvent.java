package com.ecommerce.order.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class OutboxEvent {
  private Long id;
  private String aggregateId;
  private String type;
  private String payload;
  private OutboxEventStatus status;
  private LocalDateTime createdAt;
}
