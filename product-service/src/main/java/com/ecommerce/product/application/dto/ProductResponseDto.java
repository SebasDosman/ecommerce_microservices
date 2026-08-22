package com.ecommerce.product.application.dto;

public record ProductResponseDto(
    String id,
    String name,
    String description,
    Double price
) {}
