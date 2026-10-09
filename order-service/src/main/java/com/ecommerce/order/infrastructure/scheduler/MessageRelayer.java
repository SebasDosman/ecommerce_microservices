package com.ecommerce.order.infrastructure.scheduler;

import com.ecommerce.order.application.service.IGetOutboxEventUseCase;
import com.ecommerce.order.application.service.IUpdateOutboxEventUseCase;
import com.ecommerce.order.domain.model.OutboxEvent;
import com.ecommerce.order.domain.model.OutboxEventStatus;
import com.ecommerce.order.infrastructure.config.RabbitMQConfig;
import com.ecommerce.order.infrastructure.event.OrderEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class MessageRelayer {
  private final IGetOutboxEventUseCase getOutboxEventUseCase;
  private final IUpdateOutboxEventUseCase updateOutboxEventUseCase;
  private final RabbitTemplate rabbitTemplate;
  private final ObjectMapper objectMapper;

  @Scheduled(fixedRate = 10000)
  public void relayMessage() {
    List<OutboxEvent> pendingOutboxEvents =
        getOutboxEventUseCase.getAllByStatus(OutboxEventStatus.PENDING);

    if (!pendingOutboxEvents.isEmpty()) {
      log.info("Found {} pending outbox events to relay.", pendingOutboxEvents.size());

      for (OutboxEvent outboxEvent : pendingOutboxEvents) {
        try {
          updateOutboxEventUseCase.updateStatus(outboxEvent.getId(), OutboxEventStatus.PROCESSING);
          OrderEvent orderEvent =
              objectMapper.readValue(outboxEvent.getPayload(), OrderEvent.class);
          rabbitTemplate.convertAndSend(
              RabbitMQConfig.ORDER_EXCHANGE_NAME,
              routingKeyFor(orderEvent.type()),
              orderEvent);
          updateOutboxEventUseCase.updateStatus(outboxEvent.getId(), OutboxEventStatus.PUBLISHED);
          log.info("Successfully relayed outbox event with ID: {}", outboxEvent.getId());
        } catch (JacksonException e) {
          updateOutboxEventUseCase.updateStatus(outboxEvent.getId(), OutboxEventStatus.FAILED);
          log.error("Failed to deserialize outbox event with ID: {}", outboxEvent.getId(), e);
        } catch (AmqpException e) {
          // RabbitMQ outages are transient. Keep the event pending so the next scheduled
          // execution can retry after the broker becomes available again.
          updateOutboxEventUseCase.updateStatus(outboxEvent.getId(), OutboxEventStatus.PENDING);
          log.warn(
              "RabbitMQ unavailable for outbox event {}. It will be retried.",
              outboxEvent.getId(),
              e);
        } catch (IllegalArgumentException e) {
          updateOutboxEventUseCase.updateStatus(outboxEvent.getId(), OutboxEventStatus.FAILED);
          log.error("Unsupported outbox event with ID: {}", outboxEvent.getId(), e);
        }
      }
    }
  }

  private String routingKeyFor(String eventType) {
    return switch (eventType) {
      case OrderEvent.PLACED -> RabbitMQConfig.ORDER_PLACED_ROUTING_KEY;
      case OrderEvent.CONFIRMED -> RabbitMQConfig.ORDER_CONFIRMED_ROUTING_KEY;
      case OrderEvent.CANCELLED -> RabbitMQConfig.ORDER_CANCELLED_ROUTING_KEY;
      default -> throw new IllegalArgumentException("Unsupported order event type: " + eventType);
    };
  }
}
