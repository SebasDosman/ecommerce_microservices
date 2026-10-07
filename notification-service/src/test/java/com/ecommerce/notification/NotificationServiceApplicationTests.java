package com.ecommerce.notification;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

/**
 * Basic smoke test to verify application context loads.
 *
 * Note: This test requires RabbitMQ running on localhost:5672 or appropriate test configuration.
 * For local development, either:
 * 1. Run RabbitMQ via Docker: docker run -d -p 5672:5672 -p 15672:15672 rabbitmq:3-management
 * 2. Or mock RabbitMQ dependencies in a dedicated test configuration
 */
@SpringBootTest
@TestPropertySource(
    properties = {
      "spring.cloud.discovery.enabled=false",
      "eureka.client.enabled=false",
      "spring.mail.host=localhost",
      "spring.mail.port=3025",
      "spring.mail.username=test",
      "spring.mail.password=test",
      "spring.rabbitmq.host=localhost",
      "spring.rabbitmq.port=5672",
      "spring.rabbitmq.username=guest",
      "spring.rabbitmq.password=guest"
    })
class NotificationServiceApplicationTests {

  @Test
  void contextLoads() {}
}
