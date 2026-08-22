package com.ecommerce.product.application.service.implementation;

import com.ecommerce.product.application.dto.ProductResponseDto;
import com.ecommerce.product.application.mapper.ProductMapper;
import com.ecommerce.product.application.service.IGetProductUseCase;
import com.ecommerce.product.application.validator.ProductValidator;
import com.ecommerce.product.domain.repository.ProductRepository;
import com.ecommerce.product.shared.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetProductUseCase implements IGetProductUseCase {
  private final ProductRepository productRepository;
  private final ProductMapper productMapper;

  @Override
  public Slice<ProductResponseDto> getAll(Pageable pageable) {
    log.info(
        "Fetching all products with pagination: page number = {}, page size = {}",
        pageable.getPageNumber(),
        pageable.getPageSize());

    return productRepository.findAll(pageable).map(productMapper::toProductResponseDto);
  }

  @Override
  public ProductResponseDto getById(String id) {
    log.info("Fetching product with id: {}", id);

    return productMapper.toProductResponseDto(
        productRepository
            .findById(id)
            .orElseThrow(
                () ->
                    new NotFoundException(String.format(ProductValidator.PRODUCT_NOT_FOUND, id))));
  }
}
