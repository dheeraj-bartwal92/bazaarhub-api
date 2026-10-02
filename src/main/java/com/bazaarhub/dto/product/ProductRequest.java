package com.bazaarhub.dto.product;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProductRequest(
        @NotBlank String name,
        String description,
        Long brandId,
        String sku,
        String imageUrl,
        @NotNull Long categoryId
) {}
