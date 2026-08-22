package com.ecommerce.product.infrastructure.persistence;

import com.ecommerce.product.domain.model.Product;
import com.ecommerce.product.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProductRepositoryAdapter implements ProductRepository {
  private final ProductEntityRepository productEntityRepository;
  private final ProductEntityMapper productEntityMapper;

  @Override
  public Slice<Product> findAll(Pageable pageable) {
    return productEntityRepository.findAll(pageable).map(productEntityMapper::toDomainModel);
  }

  @Override
  public Optional<Product> findById(String id) {
    return productEntityRepository.findById(id).map(productEntityMapper::toDomainModel);
  }

  @Override
  public Product save(Product product) {
    ProductEntity entity = productEntityMapper.toEntity(product);
    ProductEntity saved = productEntityRepository.save(entity);
    return productEntityMapper.toDomainModel(saved);
  }

  @Override
  public void deleteById(String id) {
    productEntityRepository.deleteById(id);
  }

  @Override
  public boolean existsById(String id) {
    return productEntityRepository.existsById(id);
  }
}
