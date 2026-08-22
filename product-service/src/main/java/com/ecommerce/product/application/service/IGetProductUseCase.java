package com.ecommerce.product.application.service;

import com.ecommerce.product.application.dto.ProductResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface IGetProductUseCase {
  Slice<ProductResponseDto> getAll(Pageable pageable);
  ProductResponseDto getById(String id);
}
