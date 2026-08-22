package com.ecommerce.product.application.service;

import com.ecommerce.product.application.dto.ProductRequestDto;
import com.ecommerce.product.application.dto.ProductResponseDto;

public interface IUpdateProductUseCase {
  ProductResponseDto update(String id, ProductRequestDto productRequestDto);
}
