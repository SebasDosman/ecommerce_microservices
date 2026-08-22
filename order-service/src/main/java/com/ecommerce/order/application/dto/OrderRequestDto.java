package com.ecommerce.order.application.dto;

import com.ecommerce.order.application.validator.OrderValidator;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OrderRequestDto {
  @NotEmpty(message = OrderValidator.ORDER_ITEMS_REQUIRED)
  @Valid
  private List<OrderItemRequestDto> orderItems;
}
