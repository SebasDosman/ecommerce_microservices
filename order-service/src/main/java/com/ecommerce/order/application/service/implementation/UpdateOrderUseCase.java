package com.ecommerce.order.application.service.implementation;

import com.ecommerce.order.application.dto.OrderResponseDto;
import com.ecommerce.order.application.mapper.OrderMapper;
import com.ecommerce.order.application.service.IUpdateOrderUseCase;
import com.ecommerce.order.application.validator.OrderValidator;
import com.ecommerce.order.domain.model.Order;
import com.ecommerce.order.domain.model.OrderStatus;
import com.ecommerce.order.domain.repository.OrderRepository;
import com.ecommerce.order.shared.exception.NotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
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

    return orderMapper.toOrderResponseDto(orderRepository.save(order));
  }
}
