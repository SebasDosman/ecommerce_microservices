package com.ecommerce.notification.infrastructure.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
  public static final String ORDER_EXCHANGE_NAME = "order-events";
  public static final String DEAD_LETTER_EXCHANGE_NAME = "order-events-dead-letter";
  public static final String NOTIFICATION_ORDER_EVENTS_QUEUE_NAME =
      "notification-order-events-queue";
  public static final String NOTIFICATION_DEAD_LETTER_QUEUE_NAME = "notification-dead-letter-queue";
  public static final String ORDER_PLACED_ROUTING_KEY = "order.placed";
  public static final String ORDER_CONFIRMED_ROUTING_KEY = "order.confirmed";
  public static final String ORDER_CANCELLED_ROUTING_KEY = "order.cancelled";
  public static final String NOTIFICATION_DEAD_LETTER_ROUTING_KEY = "notification.dead-letter";

  @Bean
  public MessageConverter messageConverter() {
    return new JacksonJsonMessageConverter();
  }

  @Bean
  public TopicExchange orderEventExchange() {
    return new TopicExchange(ORDER_EXCHANGE_NAME);
  }

  @Bean
  public DirectExchange deadLetterExchange() {
    return new DirectExchange(DEAD_LETTER_EXCHANGE_NAME);
  }

  @Bean
  public Queue notificationOrderEventsQueue() {
    return QueueBuilder.durable(NOTIFICATION_ORDER_EVENTS_QUEUE_NAME)
        .withArgument("x-dead-letter-exchange", DEAD_LETTER_EXCHANGE_NAME)
        .withArgument("x-dead-letter-routing-key", NOTIFICATION_DEAD_LETTER_ROUTING_KEY)
        .build();
  }

  @Bean
  public Queue deadLetterQueue() {
    return new Queue(NOTIFICATION_DEAD_LETTER_QUEUE_NAME, true);
  }

  @Bean
  public Binding orderPlacedBinding(
      Queue notificationOrderEventsQueue, TopicExchange orderEventExchange) {
    return BindingBuilder.bind(notificationOrderEventsQueue)
        .to(orderEventExchange)
        .with(ORDER_PLACED_ROUTING_KEY);
  }

  @Bean
  public Binding orderConfirmedBinding(
      Queue notificationOrderEventsQueue, TopicExchange orderEventExchange) {
    return BindingBuilder.bind(notificationOrderEventsQueue)
        .to(orderEventExchange)
        .with(ORDER_CONFIRMED_ROUTING_KEY);
  }

  @Bean
  public Binding orderCancelledBinding(
      Queue notificationOrderEventsQueue, TopicExchange orderEventExchange) {
    return BindingBuilder.bind(notificationOrderEventsQueue)
        .to(orderEventExchange)
        .with(ORDER_CANCELLED_ROUTING_KEY);
  }

  @Bean
  public Binding deadLetterBinding(Queue deadLetterQueue, DirectExchange deadLetterExchange) {
    return BindingBuilder.bind(deadLetterQueue)
        .to(deadLetterExchange)
        .with(NOTIFICATION_DEAD_LETTER_ROUTING_KEY);
  }
}
