package com.ecommerce.order.application.service.implementation;

import com.ecommerce.order.application.dto.OrderResponseDto;
import com.ecommerce.order.application.mapper.OrderMapper;
import com.ecommerce.order.application.service.IGetOrderUseCase;
import com.ecommerce.order.application.validator.OrderValidator;
import com.ecommerce.order.domain.repository.OrderRepository;
import com.ecommerce.order.shared.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetOrderUseCase implements IGetOrderUseCase {
  private final OrderRepository orderRepository;
  private final OrderMapper orderMapper;

  @Override
  @Transactional(readOnly = true)
  public Slice<OrderResponseDto> getAll(Pageable pageable) {
    log.info(
        "Fetching all orders with pagination: page number = {}, page size = {}",
        pageable.getPageNumber(),
        pageable.getPageSize());

    return orderRepository.findAll(pageable).map(orderMapper::toOrderResponseDto);
  }

  @Override
  @Transactional(readOnly = true)
  public OrderResponseDto getById(Long id) {
    log.info("Fetching order with id: {}", id);

    return orderRepository
        .findById(id)
        .map(orderMapper::toOrderResponseDto)
        .orElseThrow(
            () -> new NotFoundException(String.format(OrderValidator.ORDER_NOT_FOUND, id)));
  }

  @Override
  @Transactional(readOnly = true)
  public Slice<OrderResponseDto> getByUserId(Pageable pageable, String userId) {
    return orderRepository.findByUserId(pageable, userId).map(orderMapper::toOrderResponseDto);
  }
}
