package com.ecommerce.product.application.mapper;

import com.ecommerce.product.application.dto.ProductRequestDto;
import com.ecommerce.product.application.dto.ProductResponseDto;
import com.ecommerce.product.domain.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {
  ProductResponseDto toProductResponseDto(Product product);

  @Mapping(target = "id", ignore = true)
  Product toProduct(ProductRequestDto productRequestDto);

  @Mapping(target = "id", ignore = true)
  void updateProduct(ProductRequestDto productRequestDto, @MappingTarget Product product);
}
