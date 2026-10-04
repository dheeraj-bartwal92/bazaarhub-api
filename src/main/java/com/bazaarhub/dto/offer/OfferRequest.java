package com.bazaarhub.dto.offer;

import com.bazaarhub.entity.ProductCondition;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record OfferRequest(
        @NotNull Long productId,
        @NotNull Long sellerId,
        @NotNull @Positive BigDecimal price,
        @PositiveOrZero int stockQuantity,
        ProductCondition condition
) {}