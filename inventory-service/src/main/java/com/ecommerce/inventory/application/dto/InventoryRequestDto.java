package com.ecommerce.inventory.application.dto;

import com.ecommerce.inventory.application.validator.InventoryValidator;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class InventoryRequestDto {
    @NotBlank(message = InventoryValidator.INVENTORY_SKU_REQUIRED)
    String sku;

    @NotNull(message = InventoryValidator.INVENTORY_QUANTITY_REQUIRED)
    @Positive(message = InventoryValidator.INVENTORY_QUANTITY_POSITIVE)
    Integer quantity;
}
