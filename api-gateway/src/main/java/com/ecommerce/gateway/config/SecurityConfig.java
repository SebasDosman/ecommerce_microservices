package com.ecommerce.gateway.config;

import com.ecommerce.gateway.enums.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;

import java.util.*;
import java.util.stream.Collectors;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
  @Bean
  public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity serverHttpSecurity) {
    return serverHttpSecurity
        .csrf(ServerHttpSecurity.CsrfSpec::disable)
        .authorizeExchange(
            authorizeExchangeSpec ->
                authorizeExchangeSpec
                    .pathMatchers("/eureka/**")
                    .permitAll()
                    .pathMatchers(HttpMethod.GET, "/api/v1/products/**")
                    .permitAll()
                    .pathMatchers("/api/v1/products/**")
                    .hasRole(Role.ADMIN.name())
                    .pathMatchers(HttpMethod.GET, "/api/v1/inventory/**")
                    .permitAll()
                    .pathMatchers("/api/v1/inventory/**")
                    .hasRole(Role.ADMIN.name())
                    // A USER can create orders and only read its own orders.
                    .pathMatchers(HttpMethod.GET, "/api/v1/orders/user")
                    .hasRole(Role.USER.name())
                    .pathMatchers(HttpMethod.POST, "/api/v1/orders", "/api/v1/orders/")
                    .hasRole(Role.USER.name())
                    // Only an ADMIN can list all orders or read an order by id.
                    .pathMatchers(HttpMethod.GET, "/api/v1/orders", "/api/v1/orders/")
                    .hasRole(Role.ADMIN.name())
                    .pathMatchers(HttpMethod.GET, "/api/v1/orders/*")
                    .hasRole(Role.ADMIN.name())
                    .pathMatchers(HttpMethod.DELETE, "/api/v1/orders/*")
                    .hasRole(Role.ADMIN.name())
                    .anyExchange()
                    .authenticated())
        .oauth2ResourceServer(
            oAuth2ResourceServerSpec ->
                oAuth2ResourceServerSpec.jwt(
                    jwtSpec ->
                        jwtSpec.jwtAuthenticationConverter(
                            reactiveJwtAuthenticationConverterAdapter())))
        .build();
  }

  private ReactiveJwtAuthenticationConverterAdapter reactiveJwtAuthenticationConverterAdapter() {
    JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();

    jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(
        jwt -> {
          Object realmAccessClaim = jwt.getClaims().get("realm_access");
          if (!(realmAccessClaim instanceof Map<?, ?> realmAccess)) {
            return Collections.emptyList();
          }

          Object rolesClaim = realmAccess.get("roles");
          if (!(rolesClaim instanceof Collection<?> roles)) {
            return Collections.emptyList();
          }

          return roles.stream()
              .filter(Objects::nonNull)
              .map(Object::toString)
              .map(role -> role.startsWith("ROLE_") ? role : "ROLE_" + role.toUpperCase(Locale.ROOT))
              .map(SimpleGrantedAuthority::new)
              .collect(Collectors.toList());
        });

    return new ReactiveJwtAuthenticationConverterAdapter(jwtAuthenticationConverter);
  }
}
