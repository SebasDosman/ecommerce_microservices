package com.ecommerce.inventory.infrastructure.event;

import lombok.Builder;

@Builder
public record OrderConfirmedEvent(String orderNumber, String email) {}
