package com.ecommerce.order.infrastructure.config;

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
  public static final String ORDER_COMPLETED_QUEUE_NAME = "order-completed-queue";
  public static final String ORDER_CANCELLED_QUEUE_NAME = "order-cancelled-queue";
  public static final String ORDER_EXCHANGE_NAME = "order-events";
  public static final String ORDER_PLACED_ROUTING_KEY = "order.placed";
  public static final String ORDER_CANCELLED_ROUTING_KEY = "order.cancelled";
  public static final String ORDER_COMPLETED_ROUTING_KEY = "order.completed";

  @Bean
  public MessageConverter messageConverter() {
    return new JacksonJsonMessageConverter();
  }

  @Bean
  public TopicExchange topicExchange() {
    return new TopicExchange(ORDER_EXCHANGE_NAME);
  }

  @Bean
  public Queue orderCompletedQueue() {
    return new Queue(ORDER_COMPLETED_QUEUE_NAME, true);
  }

  @Bean
  public Queue orderCancelledQueue() {
    return new Queue(ORDER_CANCELLED_QUEUE_NAME, true);
  }

  @Bean
  public Binding orderCompletedBinding(Queue orderCompletedQueue, TopicExchange topicExchange) {
    return BindingBuilder.bind(orderCompletedQueue).to(topicExchange).with(ORDER_COMPLETED_ROUTING_KEY);
  }

  @Bean
  public Binding orderCancelledBinding(Queue orderCancelledQueue, TopicExchange topicExchange) {
    return BindingBuilder.bind(orderCancelledQueue).to(topicExchange).with(ORDER_CANCELLED_ROUTING_KEY);
  }
}
