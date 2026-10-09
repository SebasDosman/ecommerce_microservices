package com.ecommerce.order.domain.repository;

import com.ecommerce.order.domain.model.OutboxEvent;
import com.ecommerce.order.domain.model.OutboxEventStatus;

import java.util.List;
import java.util.Optional;

public interface OutboxEventRepository {
  List<OutboxEvent> findAllByStatus(OutboxEventStatus status);
  Optional<OutboxEvent> findById(Long id);
  void save(OutboxEvent outboxEvent);
}
