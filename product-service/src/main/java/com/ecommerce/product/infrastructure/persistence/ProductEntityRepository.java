package com.ecommerce.product.infrastructure.persistence;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
interface ProductEntityRepository extends MongoRepository<ProductEntity, String> {
}
