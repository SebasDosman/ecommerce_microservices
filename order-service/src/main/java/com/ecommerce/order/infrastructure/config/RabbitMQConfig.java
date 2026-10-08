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
  public static final String ORDER_STATUS_EVENTS_QUEUE_NAME = "order-status-events-queue";
  public static final String ORDER_EXCHANGE_NAME = "order-events";
  public static final String ORDER_PLACED_ROUTING_KEY = "order.placed";
  public static final String ORDER_CANCELLED_ROUTING_KEY = "order.cancelled";
  public static final String ORDER_CONFIRMED_ROUTING_KEY = "order.confirmed";

  @Bean
  public MessageConverter messageConverter() {
    return new JacksonJsonMessageConverter();
  }

  @Bean
  public TopicExchange topicExchange() {
    return new TopicExchange(ORDER_EXCHANGE_NAME);
  }

  @Bean
  public Queue orderStatusEventsQueue() {
    return new Queue(ORDER_STATUS_EVENTS_QUEUE_NAME, true);
  }

  @Bean
  public Binding orderConfirmedBinding(Queue orderStatusEventsQueue, TopicExchange topicExchange) {
    return BindingBuilder.bind(orderStatusEventsQueue).to(topicExchange).with(ORDER_CONFIRMED_ROUTING_KEY);
  }

  @Bean
  public Binding orderCancelledBinding(Queue orderStatusEventsQueue, TopicExchange topicExchange) {
    return BindingBuilder.bind(orderStatusEventsQueue).to(topicExchange).with(ORDER_CANCELLED_ROUTING_KEY);
  }
}
