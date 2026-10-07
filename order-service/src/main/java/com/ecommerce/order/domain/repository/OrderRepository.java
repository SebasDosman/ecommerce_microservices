package com.ecommerce.order.domain.repository;

import com.ecommerce.order.domain.model.Order;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface OrderRepository {
  Slice<Order> findAll(Pageable pageable);
  Optional<Order> findById(Long id);
  Order save(Order order);
  void deleteById(Long id);
  boolean existsById(Long id);
  Slice<Order> findByUserId(Pageable pageable, String userId);
  Optional<Order> findByOrderNumber(String orderNumber);
}
