package com.ecommerce.product.application.validator;

public class ProductValidator {
    private ProductValidator() {
        /* This utility class should not be instantiated */
    }

    public static final String PRODUCT_NAME_REQUIRED = "Product name is required";
    public static final String PRODUCT_NAME_LENGTH = "Product name must be between 3 and 100 characters";

    public static final String PRODUCT_PRICE_REQUIRED = "Product price is required";
    public static final String PRODUCT_PRICE_POSITIVE = "Product price must be a positive number";

    public static final String PRODUCT_NOT_FOUND = "Product with ID %s not found";
}

