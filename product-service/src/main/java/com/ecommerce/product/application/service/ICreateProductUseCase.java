package com.ecommerce.product.application.service;

import com.ecommerce.product.application.dto.ProductRequestDto;
import com.ecommerce.product.application.dto.ProductResponseDto;

public interface ICreateProductUseCase {
  ProductResponseDto create(ProductRequestDto productRequestDto);
}
