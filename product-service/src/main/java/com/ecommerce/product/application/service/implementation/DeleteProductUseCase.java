package com.ecommerce.product.application.service.implementation;

import com.ecommerce.product.application.service.IDeleteProductUseCase;
import com.ecommerce.product.application.validator.ProductValidator;
import com.ecommerce.product.domain.repository.ProductRepository;
import com.ecommerce.product.shared.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeleteProductUseCase implements IDeleteProductUseCase {
  private final ProductRepository productRepository;

  @Override
  public void delete(String id) {
    if (!productRepository.existsById(id))
      throw new NotFoundException(String.format(ProductValidator.PRODUCT_NOT_FOUND, id));

    log.info("Deleting product with id: {}", id);

    productRepository.deleteById(id);
  }
}
