package com.ecommerce.order.application.service.implementation;

import com.ecommerce.order.application.service.IGetOutboxEventUseCase;
import com.ecommerce.order.domain.model.OutboxEvent;
import com.ecommerce.order.domain.model.OutboxEventStatus;
import com.ecommerce.order.domain.repository.OutboxEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetOutboxEventUseCase implements IGetOutboxEventUseCase {
  private final OutboxEventRepository outboxEventRepository;

  @Override
  public List<OutboxEvent> getAllByStatus(OutboxEventStatus status) {
    return outboxEventRepository.findAllByStatus(status);
  }
}
