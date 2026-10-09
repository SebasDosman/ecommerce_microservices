package com.ecommerce.order.application.service;

import com.ecommerce.order.domain.model.OutboxEventStatus;

public interface IUpdateOutboxEventUseCase {
    void updateStatus(Long id, OutboxEventStatus status);
}
