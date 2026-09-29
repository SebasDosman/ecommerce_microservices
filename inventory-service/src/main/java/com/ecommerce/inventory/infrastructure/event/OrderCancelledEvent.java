package com.ecommerce.inventory.infrastructure.event;

import lombok.Builder;

@Builder
public record OrderCancelledEvent(String orderNumber, String email, String cancelReason) {}
