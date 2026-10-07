package com.ecommerce.notification.application.service.implementation;

import com.ecommerce.notification.application.dto.OrderEmailCommandDto;
import com.ecommerce.notification.application.service.IOrderEmailService;
import com.ecommerce.notification.domain.model.OrderEmailItemView;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderEmailUseCase implements IOrderEmailService {
  private final JavaMailSender javaMailSender;
  private final SpringTemplateEngine templateEngine;

  @Value("${ecommerce.mail.from:no-reply@ecommerce.local}")
  private String fromEmail;

  @Override
  public void sendOrderConfirmation(OrderEmailCommandDto orderEmailCommand) {
    if (orderEmailCommand == null
        || orderEmailCommand.getEmail() == null
        || orderEmailCommand.getEmail().isBlank()) {
      log.warn("Order confirmation email skipped because recipient email is missing.");
      return;
    }

    List<OrderEmailItemView> items = buildOrderItems(orderEmailCommand);
    BigDecimal total =
        items.stream().map(OrderEmailItemView::subtotal).reduce(BigDecimal.ZERO, BigDecimal::add);

    Context context = new Context(Locale.forLanguageTag("es"));
    context.setVariable("customerEmail", orderEmailCommand.getEmail());
    context.setVariable("orderNumber", orderEmailCommand.getOrderNumber());
    context.setVariable("orderItems", items);
    context.setVariable("orderTotal", total);

    String htmlContent = templateEngine.process("order-confirmation-email", context);

    try {
      MimeMessage mimeMessage = javaMailSender.createMimeMessage();
      MimeMessageHelper helper =
          new MimeMessageHelper(
              mimeMessage,
              MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
              StandardCharsets.UTF_8.name());

      helper.setFrom(fromEmail);
      helper.setTo(orderEmailCommand.getEmail());
      helper.setSubject("Order confirmation: " + orderEmailCommand.getOrderNumber());
      helper.setText(htmlContent, true);

      javaMailSender.send(mimeMessage);
      log.info(
          "Order confirmation email sent successfully to {} for order {}",
          orderEmailCommand.getEmail(),
          orderEmailCommand.getOrderNumber());
    } catch (MessagingException ex) {
      log.error(
          "Error sending confirmation email to {} for order {}",
          orderEmailCommand.getEmail(),
          orderEmailCommand.getOrderNumber(),
          ex);
      throw new IllegalStateException("Unable to send order confirmation email", ex);
    }
  }

  private List<OrderEmailItemView> buildOrderItems(OrderEmailCommandDto orderEmailCommand) {
    List<OrderEmailItemView> orderItems = new ArrayList<>();

    if (orderEmailCommand.getOrderItems() == null || orderEmailCommand.getOrderItems().isEmpty()) {
      return orderItems;
    }

    for (OrderEmailCommandDto.OrderItemEmailDto item : orderEmailCommand.getOrderItems()) {
      BigDecimal unitPrice = parsePrice(item.getPrice());
      BigDecimal subtotal = unitPrice.multiply(BigDecimal.valueOf(item.getQuantity()));
      orderItems.add(new OrderEmailItemView(item.getSku(), item.getQuantity(), unitPrice, subtotal));
    }

    return orderItems;
  }

  private BigDecimal parsePrice(String rawPrice) {
    if (rawPrice == null || rawPrice.isBlank()) {
      return BigDecimal.ZERO;
    }

    try {
      return new BigDecimal(rawPrice.replace(",", "."));
    } catch (NumberFormatException _) {
      log.warn("Price '{}' could not be parsed for an order item. Defaulting to zero.", rawPrice);
      return BigDecimal.ZERO;
    }
  }
}
