package com.ecommerce.order.domain.model;

public enum OutboxEventStatus {
  PENDING,
  PROCESSING,
  PUBLISHED,
  FAILED
}
