package com.ecommerce.order.infrastructure.persistence;

import com.ecommerce.order.domain.model.Order;
import com.ecommerce.order.domain.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OrderRepositoryAdapter implements OrderRepository {
  private final OrderEntityRepository orderEntityRepository;
  private final OrderEntityMapper orderEntityMapper;

  @Override
  public Slice<Order> findAll(Pageable pageable) {
    return orderEntityRepository.findAll(pageable).map(orderEntityMapper::toDomainModel);
  }

  @Override
  public Optional<Order> findById(Long id) {
    return orderEntityRepository.findById(id).map(orderEntityMapper::toDomainModel);
  }

  @Override
  public Order save(Order order) {
    OrderEntity entity = orderEntityMapper.toEntity(order);
    OrderEntity saved = orderEntityRepository.save(entity);
    return orderEntityMapper.toDomainModel(saved);
  }

  @Override
  public void deleteById(Long id) {
    orderEntityRepository.deleteById(id);
  }

  @Override
  public boolean existsById(Long id) {
    return orderEntityRepository.existsById(id);
  }

  @Override
  public Slice<Order> findByUserId(Pageable pageable, String userId) {
    return orderEntityRepository
        .findByUserId(pageable, userId)
        .map(orderEntityMapper::toDomainModel);
  }
}
