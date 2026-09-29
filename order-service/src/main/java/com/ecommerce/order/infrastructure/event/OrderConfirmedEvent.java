package com.ecommerce.order.infrastructure.event;

import lombok.Builder;

@Builder
public record OrderConfirmedEvent(String orderNumber, String email) {}
