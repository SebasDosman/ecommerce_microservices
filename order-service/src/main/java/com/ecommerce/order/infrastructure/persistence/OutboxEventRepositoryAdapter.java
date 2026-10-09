package com.ecommerce.order.infrastructure.persistence;

import com.ecommerce.order.domain.model.OutboxEvent;
import com.ecommerce.order.domain.model.OutboxEventStatus;
import com.ecommerce.order.domain.repository.OutboxEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OutboxEventRepositoryAdapter implements OutboxEventRepository {
  private final OutboxEventEntityRepository outboxEventEntityRepository;
  private final OutboxEventEntityMapper outboxEventEntityMapper;

  @Override
  public List<OutboxEvent> findAllByStatus(OutboxEventStatus status) {
    return outboxEventEntityRepository.findAllByStatus(status).stream()
        .map(outboxEventEntityMapper::toDomainModel)
        .toList();
  }

  @Override
  public Optional<OutboxEvent> findById(Long id) {
    return outboxEventEntityRepository.findById(id).map(outboxEventEntityMapper::toDomainModel);
  }

  @Override
  public void save(OutboxEvent outboxEvent) {
    OutboxEventEntity entity = outboxEventEntityMapper.toEntity(outboxEvent);
    OutboxEventEntity saved = outboxEventEntityRepository.save(entity);
    outboxEventEntityMapper.toDomainModel(saved);
  }
}
