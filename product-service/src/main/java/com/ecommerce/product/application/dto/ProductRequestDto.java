package com.ecommerce.product.application.dto;

import com.ecommerce.product.application.validator.ProductValidator;
import jakarta.validation.constraints.*;

public record ProductRequestDto(
    @NotBlank(message = ProductValidator.PRODUCT_NAME_REQUIRED)
    @Size(min = 3, max = 100, message = ProductValidator.PRODUCT_NAME_LENGTH)
    String name,

    String description,

    @NotNull(message = ProductValidator.PRODUCT_PRICE_REQUIRED)
    @Positive(message = ProductValidator.PRODUCT_PRICE_POSITIVE)
    Double price
) {}

