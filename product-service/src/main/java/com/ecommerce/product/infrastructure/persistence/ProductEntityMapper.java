package com.ecommerce.product.infrastructure.persistence;

import com.ecommerce.product.domain.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductEntityMapper {
  Product toDomainModel(ProductEntity entity);

  ProductEntity toEntity(Product product);

  void updateEntity(Product product, @MappingTarget ProductEntity entity);
}
