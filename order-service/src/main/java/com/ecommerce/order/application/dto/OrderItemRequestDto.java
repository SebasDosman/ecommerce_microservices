package com.ecommerce.order.application.dto;

import com.ecommerce.order.application.validator.OrderValidator;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OrderItemRequestDto {
  @NotBlank(message = OrderValidator.ORDER_ITEM_SKU_REQUIRED)
  private String sku;

  @NotNull(message = OrderValidator.ORDER_ITEM_PRICE_REQUIRED)
  @Positive(message = OrderValidator.ORDER_ITEM_PRICE_POSITIVE)
  private BigDecimal price;

  @NotNull(message = OrderValidator.ORDER_ITEM_QUANTITY_REQUIRED)
  @Positive(message = OrderValidator.ORDER_ITEM_QUANTITY_POSITIVE)
  private Integer quantity;
}
