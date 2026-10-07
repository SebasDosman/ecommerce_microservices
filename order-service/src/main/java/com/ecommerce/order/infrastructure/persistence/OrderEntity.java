package com.ecommerce.order.infrastructure.persistence;

import com.ecommerce.order.domain.model.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String orderNumber;
  private String userId;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "order_id")
  private List<OrderItemEntity> orderItems;

  @Enumerated(EnumType.STRING)
  private OrderStatus status;
}
