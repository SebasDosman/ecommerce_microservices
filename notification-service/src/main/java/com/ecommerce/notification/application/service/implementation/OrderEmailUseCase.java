package com.ecommerce.notification.application.service.implementation;

import com.ecommerce.notification.application.dto.OrderCancelledEmailCommandDto;
import com.ecommerce.notification.application.dto.OrderConfirmedEmailCommandDto;
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
  public void sendOrderPlaced(OrderEmailCommandDto orderEmailCommand) {
    if (!isValidEmailCommand(orderEmailCommand)) {
      log.warn("Order placed email skipped because recipient email is missing.");
      return;
    }

    List<OrderEmailItemView> items = buildOrderItems(orderEmailCommand);
    BigDecimal total =
        items.stream().map(OrderEmailItemView::subtotal).reduce(BigDecimal.ZERO, BigDecimal::add);

    Context context = buildEmailContext(orderEmailCommand.getEmail(), orderEmailCommand.getOrderNumber());
    context.setVariable("orderItems", items);
    context.setVariable("orderTotal", total);

    sendEmail(
        orderEmailCommand.getEmail(),
        "order-placed-email",
        "Order placed: " + orderEmailCommand.getOrderNumber(),
        context);
  }

  @Override
  public void sendOrderConfirmed(OrderConfirmedEmailCommandDto orderEmailCommand) {
    if (orderEmailCommand == null
        || orderEmailCommand.getEmail() == null
        || orderEmailCommand.getEmail().isBlank()) {
      log.warn("Order confirmed email skipped because recipient email is missing.");
      return;
    }

    List<OrderEmailItemView> items = buildOrderItemsFromConfirmed(orderEmailCommand);
    BigDecimal total =
        items.stream().map(OrderEmailItemView::subtotal).reduce(BigDecimal.ZERO, BigDecimal::add);

    Context context = buildEmailContext(orderEmailCommand.getEmail(), orderEmailCommand.getOrderNumber());
    context.setVariable("orderItems", items);
    context.setVariable("orderTotal", total);

    sendEmail(
        orderEmailCommand.getEmail(),
        "order-confirmed-email",
        "Order confirmed: " + orderEmailCommand.getOrderNumber(),
        context);
  }

  @Override
  public void sendOrderCancelled(OrderCancelledEmailCommandDto orderEmailCommand) {
    if (orderEmailCommand == null
        || orderEmailCommand.getEmail() == null
        || orderEmailCommand.getEmail().isBlank()) {
      log.warn("Order cancelled email skipped because recipient email is missing.");
      return;
    }

    Context context = buildEmailContext(orderEmailCommand.getEmail(), orderEmailCommand.getOrderNumber());
    context.setVariable("cancelReason", orderEmailCommand.getCancelReason());
    context.setVariable("orderItems", new ArrayList<>());
    context.setVariable("orderTotal", BigDecimal.ZERO);

    sendEmail(
        orderEmailCommand.getEmail(),
        "order-cancelled-email",
        "Order cancelled: " + orderEmailCommand.getOrderNumber(),
        context);
  }

  private Context buildEmailContext(String email, String orderNumber) {
    Context context = new Context(Locale.ENGLISH);
    context.setVariable("customerEmail", email);
    context.setVariable("orderNumber", orderNumber);
    return context;
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

  private List<OrderEmailItemView> buildOrderItemsFromConfirmed(
      OrderConfirmedEmailCommandDto orderEmailCommand) {
    List<OrderEmailItemView> orderItems = new ArrayList<>();

    if (orderEmailCommand.getOrderItems() == null || orderEmailCommand.getOrderItems().isEmpty()) {
      return orderItems;
    }

    for (OrderConfirmedEmailCommandDto.OrderItemEmailDto item : orderEmailCommand.getOrderItems()) {
      BigDecimal unitPrice = parsePrice(item.getPrice());
      BigDecimal subtotal = unitPrice.multiply(BigDecimal.valueOf(item.getQuantity()));
      orderItems.add(new OrderEmailItemView(item.getSku(), item.getQuantity(), unitPrice, subtotal));
    }

    return orderItems;
  }

  private boolean isValidEmailCommand(OrderEmailCommandDto command) {
    return command != null && command.getEmail() != null && !command.getEmail().isBlank();
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

  private void sendEmail(String recipient, String templateName, String subject, Context context) {
    try {
      String htmlContent = templateEngine.process(templateName, context);
      MimeMessage mimeMessage = javaMailSender.createMimeMessage();
      MimeMessageHelper helper =
          new MimeMessageHelper(
              mimeMessage,
              MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
              StandardCharsets.UTF_8.name());

      helper.setFrom(fromEmail);
      helper.setTo(recipient);
      helper.setSubject(subject);
      helper.setText(htmlContent, true);

      javaMailSender.send(mimeMessage);
      log.info("Email sent successfully to {} with subject: {}", recipient, subject);
    } catch (MessagingException ex) {
      log.error("Error sending email to {} with subject: {}", recipient, subject, ex);
      throw new IllegalStateException("Unable to send email: " + subject, ex);
    }
  }
}
