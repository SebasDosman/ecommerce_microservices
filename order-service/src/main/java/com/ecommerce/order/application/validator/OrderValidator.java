package com.ecommerce.order.application.validator;

public class OrderValidator {
  private OrderValidator() {
    /* This utility class should not be instantiated */
  }

  public static final String ORDER_ITEMS_REQUIRED = "Order items are required";
  public static final String ORDER_ITEM_SKU_REQUIRED = "Order item SKU is required";
  public static final String ORDER_ITEM_PRICE_REQUIRED = "Order item price is required";
  public static final String ORDER_ITEM_PRICE_POSITIVE = "Order item price must be a positive number";
  public static final String ORDER_ITEM_QUANTITY_REQUIRED = "Order item quantity is required";
  public static final String ORDER_ITEM_QUANTITY_POSITIVE = "Order item quantity must be a positive number";

  public static final String EMAIL_INVALID = "Email does not have a valid format";
  public static final String EMAIL_REQUIRED = "Email is required";

  public static final String ORDER_NOT_FOUND = "Order with ID %s not found";
  public static final String ORDER_WITH_ORDER_NUMBER_NOT_FOUND = "Order with order number %s not found";
  public static final String ORDER_IS_NOT_IN_STOCK = "Order item with SKU %s is not in stock for quantity %d";
}
