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
  public static final String NOTIFICATION_QUEUE_NAME = "order-placed-queue";
  public static final String ORDER_CONFIRMED_QUEUE_NAME = "order-confirmed-queue";
  public static final String ORDER_CANCELLED_QUEUE_NAME = "order-cancelled-queue";
  
  public static final String ORDER_PLACED_ROUTING_KEY = "order.placed";
  public static final String ORDER_CONFIRMED_ROUTING_KEY = "order.confirmed";
  public static final String ORDER_CANCELLED_ROUTING_KEY = "order.cancelled";

  @Bean
  public MessageConverter messageConverter() {
    return new JacksonJsonMessageConverter();
  }

  @Bean
  public TopicExchange orderEventExchange() {
    return new TopicExchange(ORDER_EXCHANGE_NAME);
  }

  @Bean
  public Queue orderPlacedQueue() {
    return new Queue(NOTIFICATION_QUEUE_NAME, true);
  }

  @Bean
  public Queue orderConfirmedQueue() {
    return new Queue(ORDER_CONFIRMED_QUEUE_NAME, true);
  }

  @Bean
  public Queue orderCancelledQueue() {
    return new Queue(ORDER_CANCELLED_QUEUE_NAME, true);
  }

  @Bean
  public Binding orderPlacedBinding(Queue orderPlacedQueue, TopicExchange orderEventExchange) {
    return BindingBuilder.bind(orderPlacedQueue).to(orderEventExchange).with(ORDER_PLACED_ROUTING_KEY);
  }

  @Bean
  public Binding orderConfirmedBinding(Queue orderConfirmedQueue, TopicExchange orderEventExchange) {
    return BindingBuilder.bind(orderConfirmedQueue).to(orderEventExchange).with(ORDER_CONFIRMED_ROUTING_KEY);
  }

  @Bean
  public Binding orderCancelledBinding(Queue orderCancelledQueue, TopicExchange orderEventExchange) {
    return BindingBuilder.bind(orderCancelledQueue).to(orderEventExchange).with(ORDER_CANCELLED_ROUTING_KEY);
  }
}
