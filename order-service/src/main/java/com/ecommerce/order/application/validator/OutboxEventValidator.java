package com.ecommerce.order.application.validator;

public class OutboxEventValidator {
  private OutboxEventValidator() {
    /* This utility class should not be instantiated */
  }

  public static final String OUTBOX_EVENT_NOT_FOUND = "Outbox event with ID %s not found";
}
