package com.ecommerce.order.application.service.implementation;

import com.ecommerce.order.application.service.ICreateOutboxEventUseCase;
import com.ecommerce.order.domain.model.Order;
import com.ecommerce.order.domain.model.OutboxEvent;
import com.ecommerce.order.domain.model.OutboxEventStatus;
import com.ecommerce.order.domain.repository.OutboxEventRepository;
import com.ecommerce.order.infrastructure.event.OrderEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreateOutboxEventUseCase implements ICreateOutboxEventUseCase {
  private final OutboxEventRepository outboxEventRepository;
  private final ObjectMapper objectMapper;

  @Override
  @Transactional
  public void create(Order order, String email) {
    List<OrderEvent.OrderItemEvent> orderItemEvents =
        order.getOrderItems().stream()
            .map(item -> new OrderEvent.OrderItemEvent(
                item.getSku(),
                item.getPrice() != null ? item.getPrice().toPlainString() : "0.00",
                item.getQuantity()))
            .toList();
    OrderEvent orderEvent =
        new OrderEvent(OrderEvent.PLACED, order.getOrderNumber(), email, orderItemEvents, null);
    String payload = objectMapper.writeValueAsString(orderEvent);
    log.info("Creating order placed outbox event using {}", payload);

    OutboxEvent outboxEvent =
        OutboxEvent.builder()
            .type(OrderEvent.PLACED)
            .aggregateId(order.getOrderNumber())
            .status(OutboxEventStatus.PENDING)
            .payload(payload)
            .createdAt(LocalDateTime.now())
            .build();

    outboxEventRepository.save(outboxEvent);
    log.info(
        "Order placed event created successfully for order number: {}", order.getOrderNumber());
  }
}
