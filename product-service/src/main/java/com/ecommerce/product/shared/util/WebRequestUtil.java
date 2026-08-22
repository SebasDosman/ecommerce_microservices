package com.ecommerce.product.shared.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

public class WebRequestUtil {
  private WebRequestUtil() {
    /* This utility class should not be instantiated */
  }

  public static String extractInstance(WebRequest webRequest) {
    if (webRequest instanceof ServletWebRequest servletWebRequest) {
      HttpServletRequest req = servletWebRequest.getRequest();
      String query = req.getQueryString();
      return req.getRequestURL().toString() + (query != null ? "?" + query : "");
    }

    return null;
  }
}
