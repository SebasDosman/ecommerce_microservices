package com.ecommerce.notification.infrastructure.config;

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
  public static final String NOTIFICATION_QUEUE_NAME = "notification-queue";

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
  public Queue notificationQueue() {
    return new Queue(NOTIFICATION_QUEUE_NAME, true);
  }

  @Bean
  public Binding binding(Queue notificationQueue, TopicExchange orderEventExchange) {
    return BindingBuilder.bind(notificationQueue).to(orderEventExchange).with(ORDER_COMPLETED_ROUTING_KEY);
  }
}
