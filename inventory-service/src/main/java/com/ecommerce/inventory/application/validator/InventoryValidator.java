package com.ecommerce.inventory.application.validator;

public class InventoryValidator {
  private InventoryValidator() {
    /* This utility class should not be instantiated */
  }

  public static final String INVENTORY_SKU_REQUIRED = "Inventory SKU is required";
  public static final String INVENTORY_QUANTITY_REQUIRED = "Inventory quantity is required";
  public static final String INVENTORY_QUANTITY_POSITIVE =
      "Inventory quantity must be a positive number";

  public static final String INVENTORY_NOT_FOUND = "Inventory with ID %s not found";
  public static final String INVENTORY_SKU_NOT_FOUND = "Inventory with SKU %s not found";
  public static final String INVENTORY_ALREADY_EXISTS = "Inventory with SKU %s already exists";
  public static final String INSUFFICIENT_STOCK =
      "Inventory with SKU %s has insufficient stock. Available: %d, Requested: %d";
}
