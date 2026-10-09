package com.ecommerce.order.application.service.implementation;

import com.ecommerce.order.application.dto.OrderResponseDto;
import com.ecommerce.order.application.mapper.OrderMapper;
import com.ecommerce.order.application.service.IUpdateOrderUseCase;
import com.ecommerce.order.application.validator.OrderValidator;
import com.ecommerce.order.domain.model.Order;
import com.ecommerce.order.domain.model.OrderStatus;
import com.ecommerce.order.domain.repository.OrderRepository;
import com.ecommerce.order.shared.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateOrderUseCase implements IUpdateOrderUseCase {
  private final OrderRepository orderRepository;
  private final OrderMapper orderMapper;

  @Override
  @Transactional
  public OrderResponseDto updateOrderStatus(String orderNumber, OrderStatus orderStatus) {
    Order order =
        orderRepository
            .findByOrderNumber(orderNumber)
            .orElseThrow(
                () ->
                    new NotFoundException(
                        String.format(
                            OrderValidator.ORDER_WITH_ORDER_NUMBER_NOT_FOUND, orderNumber)));

    order.setStatus(orderStatus);
    log.info("Updating order status for order number: {}", orderNumber);

    return orderMapper.toOrderResponseDto(orderRepository.save(order));
  }
}
