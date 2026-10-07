package com.ecommerce.order.application.service.implementation;

import com.ecommerce.order.application.dto.OrderRequestDto;
import com.ecommerce.order.application.dto.OrderResponseDto;
import com.ecommerce.order.application.mapper.OrderItemMapper;
import com.ecommerce.order.application.mapper.OrderMapper;
import com.ecommerce.order.application.service.ICreateOrderUseCase;
import com.ecommerce.order.domain.event.DomainEventPublisher;
import com.ecommerce.order.domain.model.Order;
import com.ecommerce.order.domain.model.OrderItem;
import com.ecommerce.order.domain.model.OrderStatus;
import com.ecommerce.order.domain.repository.OrderRepository;
import com.ecommerce.order.shared.exception.InternalException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@RefreshScope
public class CreateOrderUseCase implements ICreateOrderUseCase {
  private final OrderRepository orderRepository;
  private final OrderMapper orderMapper;
  private final OrderItemMapper orderItemMapper;
  private final DomainEventPublisher eventPublisher;

  @Value("${ecommerce.services.order.enabled}")
  private boolean ordersEnabled;

  @Override
  @Transactional
  public OrderResponseDto create(OrderRequestDto orderRequestDto, String userId) {
    if (!ordersEnabled) {
      log.warn("Order creation is currently disabled.");
      throw new InternalException("Order creation is currently disabled.");
    }

    List<OrderItem> orderItems = mapOrderItems(orderRequestDto);
    Order order = buildOrder(orderItems, userId);

    log.info("Creating order: {}", order.getOrderNumber());
    Order orderSaved = orderRepository.save(order);

    eventPublisher.publishOrderPlaced(orderSaved, orderRequestDto.getEmail());

    return orderMapper.toOrderResponseDto(orderSaved);
  }

  private List<OrderItem> mapOrderItems(OrderRequestDto orderRequestDto) {
    return orderRequestDto.getOrderItems().stream().map(orderItemMapper::toOrderItem).toList();
  }

  private Order buildOrder(List<OrderItem> orderItems, String userId) {
    return Order.builder()
        .orderNumber(UUID.randomUUID().toString())
        .orderItems(orderItems)
        .userId(userId)
        .status(OrderStatus.PLACED)
        .build();
  }
}
