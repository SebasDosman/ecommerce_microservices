package com.ecommerce.notification.domain.model;

import java.math.BigDecimal;

public record OrderEmailItemView(String sku, Integer quantity, BigDecimal price, BigDecimal subtotal) {}
