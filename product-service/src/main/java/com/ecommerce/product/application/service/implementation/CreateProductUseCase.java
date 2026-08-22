package com.ecommerce.product.application.service.implementation;

import com.ecommerce.product.application.dto.ProductRequestDto;
import com.ecommerce.product.application.dto.ProductResponseDto;
import com.ecommerce.product.application.mapper.ProductMapper;
import com.ecommerce.product.application.service.ICreateProductUseCase;
import com.ecommerce.product.domain.model.Product;
import com.ecommerce.product.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreateProductUseCase implements ICreateProductUseCase {
  private final ProductRepository productRepository;
  private final ProductMapper productMapper;

  @Override
  public ProductResponseDto create(ProductRequestDto productRequestDto) {
    Product product = productMapper.toProduct(productRequestDto);

    log.info("Creating product: {}", product.getName());

    return productMapper.toProductResponseDto(productRepository.save(product));
  }
}
