package com.ecommerce.order.application.service.implementation;

import com.ecommerce.order.application.service.IUpdateOutboxEventUseCase;
import com.ecommerce.order.application.validator.OutboxEventValidator;
import com.ecommerce.order.domain.model.OutboxEvent;
import com.ecommerce.order.domain.model.OutboxEventStatus;
import com.ecommerce.order.domain.repository.OutboxEventRepository;
import com.ecommerce.order.shared.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateOutboxEventUseCase implements IUpdateOutboxEventUseCase {
  private final OutboxEventRepository outboxEventRepository;

  @Override
  @Transactional
  public void updateStatus(Long id, OutboxEventStatus status) {
    OutboxEvent outboxEvent =
        outboxEventRepository
            .findById(id)
            .orElseThrow(
                () ->
                    new NotFoundException(
                        String.format(OutboxEventValidator.OUTBOX_EVENT_NOT_FOUND, id)));

    outboxEvent.setStatus(status);
    log.info("Outbox event with id: {} changed to status {}", id, status);

    outboxEventRepository.save(outboxEvent);
  }
}
