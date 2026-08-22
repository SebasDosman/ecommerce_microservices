package com.ecommerce.order.infrastructure.client;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.PatchExchange;

public interface InventoryClient {
    @PatchExchange("/inventory/reduce-stock/{sku}")
    void reduceStock(@PathVariable String sku, @RequestParam Integer quantity);
}
