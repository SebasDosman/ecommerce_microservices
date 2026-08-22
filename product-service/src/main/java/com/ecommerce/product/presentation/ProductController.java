package com.ecommerce.product.presentation;

import com.ecommerce.product.application.dto.ProductRequestDto;
import com.ecommerce.product.application.dto.ProductResponseDto;
import com.ecommerce.product.application.service.ICreateProductUseCase;
import com.ecommerce.product.application.service.IDeleteProductUseCase;
import com.ecommerce.product.application.service.IGetProductUseCase;
import com.ecommerce.product.application.service.IUpdateProductUseCase;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
@RefreshScope
public class ProductController {
  private final ICreateProductUseCase createProductUseCase;
  private final IDeleteProductUseCase deleteProductUseCase;
  private final IUpdateProductUseCase updateProductUseCase;
  private final IGetProductUseCase getProductUseCase;

  @Value("${ecommerce.services.product.maintenance.message}")
  private String maintenanceMessage;

  @GetMapping()
  @ResponseStatus(HttpStatus.OK)
  public Slice<ProductResponseDto> getAll(@PageableDefault Pageable pageable, HttpServletResponse httpServletResponse) {
    httpServletResponse.addHeader("X-Maintenance-Message", maintenanceMessage);
    return getProductUseCase.getAll(pageable);
  }

  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public ProductResponseDto getById(@PathVariable String id, HttpServletResponse httpServletResponse) {
    httpServletResponse.addHeader("X-Maintenance-Message", maintenanceMessage);
    return getProductUseCase.getById(id);
  }

  @PostMapping()
  @ResponseStatus(HttpStatus.CREATED)
  public ProductResponseDto save(@RequestBody @Valid ProductRequestDto productRequestDto, HttpServletResponse httpServletResponse) {
    httpServletResponse.addHeader("X-Maintenance-Message", maintenanceMessage);
    return createProductUseCase.create(productRequestDto);
  }

  @PutMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public ProductResponseDto update(
      @PathVariable String id, @RequestBody @Valid ProductRequestDto productRequestDto, HttpServletResponse httpServletResponse) {
    httpServletResponse.addHeader("X-Maintenance-Message", maintenanceMessage);
    return updateProductUseCase.update(id, productRequestDto);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable String id, HttpServletResponse httpServletResponse) {
    httpServletResponse.addHeader("X-Maintenance-Message", maintenanceMessage);
    deleteProductUseCase.delete(id);
  }
}
