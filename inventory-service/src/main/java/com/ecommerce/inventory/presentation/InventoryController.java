package com.ecommerce.inventory.presentation;

import com.ecommerce.inventory.application.dto.InventoryRequestDto;
import com.ecommerce.inventory.application.dto.InventoryResponseDto;
import com.ecommerce.inventory.application.service.ICreateInventoryUseCase;
import com.ecommerce.inventory.application.service.IDeleteInventoryUseCase;
import com.ecommerce.inventory.application.service.IGetInventoryUseCase;
import com.ecommerce.inventory.application.service.IUpdateInventoryUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/inventory")
public class InventoryController {
  private final ICreateInventoryUseCase createInventoryUseCase;
  private final IDeleteInventoryUseCase deleteInventoryUseCase;
  private final IUpdateInventoryUseCase updateInventoryUseCase;
  private final IGetInventoryUseCase getInventoryUseCase;

  @GetMapping()
  @ResponseStatus(HttpStatus.OK)
  public Slice<InventoryResponseDto> getAll(@PageableDefault Pageable pageable) {
    return getInventoryUseCase.getAll(pageable);
  }

  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public InventoryResponseDto getById(@PathVariable Long id) {
    return getInventoryUseCase.getById(id);
  }

  @GetMapping("/check-stock/{sku}")
  @ResponseStatus(HttpStatus.OK)
  public boolean isInStock(@PathVariable String sku, @RequestParam Integer quantity) {
    return getInventoryUseCase.isInStock(sku, quantity);
  }

  @PatchMapping("/reduce-stock/{sku}")
  @ResponseStatus(HttpStatus.OK)
  public void reduceStock(@PathVariable String sku, @RequestParam Integer quantity) {
    updateInventoryUseCase.reduceStock(sku, quantity);
  }

  @PostMapping()
  @ResponseStatus(HttpStatus.CREATED)
  public InventoryResponseDto save(@RequestBody @Valid InventoryRequestDto inventoryRequestDto) {
    return createInventoryUseCase.create(inventoryRequestDto);
  }

  @PutMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public InventoryResponseDto update(
      @PathVariable Long id, @RequestBody @Valid InventoryRequestDto inventoryRequestDto) {
    return updateInventoryUseCase.update(id, inventoryRequestDto);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable Long id) {
    deleteInventoryUseCase.delete(id);
  }
}
