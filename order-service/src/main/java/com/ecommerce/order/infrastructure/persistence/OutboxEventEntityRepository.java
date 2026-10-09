package com.ecommerce.order.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import com.ecommerce.order.domain.model.OutboxEventStatus;

@Repository
public interface OutboxEventEntityRepository extends JpaRepository<OutboxEventEntity, Long> {
  List<OutboxEventEntity> findAllByStatus(OutboxEventStatus status);
}
