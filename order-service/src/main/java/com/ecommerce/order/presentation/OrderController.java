package com.ecommerce.order.presentation;

import com.ecommerce.order.application.dto.OrderRequestDto;
import com.ecommerce.order.application.dto.OrderResponseDto;
import com.ecommerce.order.application.service.ICreateOrderUseCase;
import com.ecommerce.order.application.service.IDeleteOrderUseCase;
import com.ecommerce.order.application.service.IGetOrderUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {
  private final ICreateOrderUseCase createOrderUseCase;
  private final IDeleteOrderUseCase deleteOrderUseCase;
  private final IGetOrderUseCase getOrderUseCase;

  @GetMapping()
  @ResponseStatus(HttpStatus.OK)
  public Slice<OrderResponseDto> getAll(@PageableDefault Pageable pageable) {
    return getOrderUseCase.getAll(pageable);
  }

  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public OrderResponseDto getById(@PathVariable Long id) {
    return getOrderUseCase.getById(id);
  }

  @GetMapping("/user")
  @ResponseStatus(HttpStatus.OK)
  public Slice<OrderResponseDto> getByUserId(
      @PageableDefault Pageable pageable, @AuthenticationPrincipal Jwt jwt) {
    return getOrderUseCase.getByUserId(pageable, jwt.getSubject());
  }

  @PostMapping()
  @ResponseStatus(HttpStatus.CREATED)
  public OrderResponseDto save(
      @RequestBody @Valid OrderRequestDto orderRequestDto, @AuthenticationPrincipal Jwt jwt) {
    return createOrderUseCase.create(orderRequestDto, jwt.getSubject());
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable Long id) {
    deleteOrderUseCase.delete(id);
  }
}
