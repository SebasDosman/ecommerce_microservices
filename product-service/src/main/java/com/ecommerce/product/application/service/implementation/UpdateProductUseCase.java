package com.ecommerce.product.application.service.implementation;

import com.ecommerce.product.application.dto.ProductRequestDto;
import com.ecommerce.product.application.dto.ProductResponseDto;
import com.ecommerce.product.application.mapper.ProductMapper;
import com.ecommerce.product.application.service.IUpdateProductUseCase;
import com.ecommerce.product.application.validator.ProductValidator;
import com.ecommerce.product.domain.model.Product;
import com.ecommerce.product.domain.repository.ProductRepository;
import com.ecommerce.product.shared.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateProductUseCase implements IUpdateProductUseCase {
  private final ProductRepository productRepository;
  private final ProductMapper productMapper;

  @Override
  public ProductResponseDto update(String id, ProductRequestDto productRequestDto) {
    Product product = productRepository.findById(id)
        .orElseThrow(() -> new NotFoundException(String.format(ProductValidator.PRODUCT_NOT_FOUND, id)));

    productMapper.updateProduct(productRequestDto, product);
    log.info("Updating product with id {}", id);

    return productMapper.toProductResponseDto(productRepository.save(product));
  }
}
