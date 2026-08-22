package com.ecommerce.order.application.service.implementation;

import com.ecommerce.order.application.dto.OrderRequestDto;
import com.ecommerce.order.application.dto.OrderResponseDto;
import com.ecommerce.order.application.mapper.OrderItemMapper;
import com.ecommerce.order.application.mapper.OrderMapper;
import com.ecommerce.order.application.service.ICreateOrderUseCase;
import com.ecommerce.order.application.validator.OrderValidator;
import com.ecommerce.order.domain.model.Order;
import com.ecommerce.order.domain.model.OrderItem;
import com.ecommerce.order.domain.repository.OrderRepository;
import com.ecommerce.order.infrastructure.client.InventoryClient;
import com.ecommerce.order.shared.exception.ConflictException;
import com.ecommerce.order.shared.exception.InternalException;
import jakarta.ws.rs.core.UriBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@RefreshScope
public class CreateOrderUseCase implements ICreateOrderUseCase {
  private final OrderRepository orderRepository;
  private final OrderMapper orderMapper;
  private final OrderItemMapper orderItemMapper;
  private final InventoryClient inventoryClient;

  @Value("${ecommerce.services.order.enabled}")
  private boolean ordersEnabled;

  @Override
  @Transactional
  public OrderResponseDto create(OrderRequestDto orderRequestDto, String userId) {
    if (!ordersEnabled) {
      log.warn("Order creation is currently disabled.");
      throw new InternalException("Order creation is currently disabled.");
    }

    List<OrderItem> orderItems =
        orderRequestDto.getOrderItems().stream()
            .map(orderItemMapper::toOrderItem)
            .map(this::validateStockAndReturn)
            .toList();

    Order order =
        Order.builder()
            .orderNumber(UUID.randomUUID().toString())
            .orderItems(orderItems)
            .userId(userId)
            .build();

    log.info("Creating order: {}", order.getOrderNumber());

    return orderMapper.toOrderResponseDto(orderRepository.save(order));
  }

  private OrderItem validateStockAndReturn(OrderItem orderItem) {
    try {
      inventoryClient.reduceStock(orderItem.getSku(), orderItem.getQuantity());
    } catch (Exception e) {
      log.error(
          "Stock validation failed for SKU: {}. Error: {}", orderItem.getSku(), e.getMessage());
      throw new ConflictException("Insufficient stock for SKU: " + orderItem.getSku());
    }

    return orderItem;
  }
}
