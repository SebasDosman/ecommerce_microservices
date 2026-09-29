package com.ecommerce.notification.infrastructure.event;

import lombok.Builder;

@Builder
public record OrderCancelledEvent(String orderNumber, String email, String cancelReason) {}
