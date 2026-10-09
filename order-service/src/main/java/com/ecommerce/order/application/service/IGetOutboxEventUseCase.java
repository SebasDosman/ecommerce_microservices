package com.ecommerce.order.application.service;

import com.ecommerce.order.domain.model.OutboxEvent;
import com.ecommerce.order.domain.model.OutboxEventStatus;

import java.util.List;

public interface IGetOutboxEventUseCase {
    List<OutboxEvent> getAllByStatus(OutboxEventStatus status);
}
