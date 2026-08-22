package com.ecommerce.product.domain.repository;

import com.ecommerce.product.domain.model.Product;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface ProductRepository {
  Slice<Product> findAll(Pageable pageable);
  Optional<Product> findById(String id);
  Product save(Product product);
  void deleteById(String id);
  boolean existsById(String id);
}
