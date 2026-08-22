package com.ecommerce.order.application.service.implementation;

import com.ecommerce.order.application.service.IDeleteOrderUseCase;
import com.ecommerce.order.application.validator.OrderValidator;
import com.ecommerce.order.domain.repository.OrderRepository;
import com.ecommerce.order.shared.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeleteOrderUseCase implements IDeleteOrderUseCase {
  private final OrderRepository orderRepository;

  @Override
  @Transactional
  public void delete(Long id) {
    if (!orderRepository.existsById(id))
      throw new NotFoundException(String.format(OrderValidator.ORDER_NOT_FOUND, id));

    log.info("Deleting order with id: {}", id);

    orderRepository.deleteById(id);
  }
}
