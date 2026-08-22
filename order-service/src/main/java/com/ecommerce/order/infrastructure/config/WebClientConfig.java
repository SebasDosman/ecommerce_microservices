package com.ecommerce.order.infrastructure.config;

import com.ecommerce.order.infrastructure.client.InventoryClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class WebClientConfig {
  @Value("${ecommerce.services.inventory.url}")
  private String inventoryServiceUrl;

  @Bean
  @LoadBalanced
  public WebClient.Builder webClientBuilder() {
    return WebClient.builder();
  }

  @Bean
  public InventoryClient inventoryClient(WebClient.Builder webClientBuilder) {
    WebClient webClient = webClientBuilder.baseUrl(inventoryServiceUrl).build();

    HttpServiceProxyFactory httpServiceProxyFactory =
        HttpServiceProxyFactory.builderFor(WebClientAdapter.create(webClient)).build();

    return httpServiceProxyFactory.createClient(InventoryClient.class);
  }
}
