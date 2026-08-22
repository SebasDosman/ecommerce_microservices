package com.ecommerce.inventory.shared.advice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import com.ecommerce.inventory.shared.exception.ConflictException;
import com.ecommerce.inventory.shared.exception.NotFoundException;
import com.ecommerce.inventory.shared.util.WebRequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
@Slf4j
public class GlobalControllerAdvice {
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public Map<String, Object> handleInvalidArgument(
      MethodArgumentNotValidException ex, WebRequest webRequest) {
    Map<String, Object> fieldErrors = new HashMap<>();

    ex.getBindingResult()
        .getFieldErrors()
        .forEach(
            error -> {
              Map<String, Object> details = new HashMap<>();
              details.put("message", error.getDefaultMessage());
              details.put("objectName", error.getObjectName());

              fieldErrors.put(error.getField(), details);
            });

    String instance = WebRequestUtil.extractInstance(webRequest);

    log.warn("Invalid argument at {}: {}", instance, fieldErrors);
    return createErrorResponse(HttpStatus.BAD_REQUEST, "Validation failed", fieldErrors, instance);
  }

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(NotFoundException.class)
  public Map<String, Object> handleNotFoundException(NotFoundException ex, WebRequest webRequest) {
    String instance = WebRequestUtil.extractInstance(webRequest);
    log.warn("Resource not found at {}: {}", instance, ex.getMessage());
    return createErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), null, instance);
  }

  @ResponseStatus(HttpStatus.CONFLICT)
  @ExceptionHandler(ConflictException.class)
  public Map<String, Object> handleConflictException(ConflictException ex, WebRequest webRequest) {
    String instance = WebRequestUtil.extractInstance(webRequest);
    log.warn("Resource conflict at {}: {}", instance, ex.getMessage());
    return createErrorResponse(HttpStatus.CONFLICT, ex.getMessage(), null, instance);
  }

  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(Exception.class)
  public Map<String, Object> handleException(Exception ex, WebRequest webRequest) {
    String instance = WebRequestUtil.extractInstance(webRequest);
    log.error("Unhandled exception at {}: {}", instance, ex.getMessage(), ex);
    return createErrorResponse(
        HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error", null, instance);
  }

  private Map<String, Object> createErrorResponse(
      HttpStatus status, String message, Map<String, Object> fieldErrors, String instance) {
    Map<String, Object> response = new HashMap<>();
    response.put("status", status.getReasonPhrase());
    response.put("code", status.value());
    response.put("message", message);
    response.put("timestamp", Instant.now().toString());

    if (instance != null) {
      response.put("instance", instance);
    }
    if (fieldErrors != null && !fieldErrors.isEmpty()) {
      response.put("errors", fieldErrors);
    }

    return response;
  }
}
