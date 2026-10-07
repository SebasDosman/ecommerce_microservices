package com.ecommerce.inventory.infrastructure.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
  public static final String ORDER_EXCHANGE_NAME = "order-events";
  public static final String INVENTORY_QUEUE_NAME = "inventory-queue";
  public static final String ORDER_PLACED_ROUTING_KEY = "order.placed";
  public static final String ORDER_CANCELLED_ROUTING_KEY = "order.cancelled";
  public static final String ORDER_COMPLETED_ROUTING_KEY = "order.completed";

  @Bean
  public MessageConverter messageConverter() {
    return new JacksonJsonMessageConverter();
  }

  @Bean
  public TopicExchange orderEventExchange() {
    return new TopicExchange(ORDER_EXCHANGE_NAME);
  }

  @Bean
  public Queue inventoryQueue() {
    return new Queue(INVENTORY_QUEUE_NAME, true);
  }

  @Bean
  public Binding binding(Queue inventoryQueue, TopicExchange orderEventExchange) {
    return BindingBuilder.bind(inventoryQueue).to(orderEventExchange).with(ORDER_PLACED_ROUTING_KEY);
  }
}
