package com.bazaarhub.dto.offer;

import com.bazaarhub.entity.ProductCondition;

import java.math.BigDecimal;

public record OfferResponse(
        Long id,
        Long productId,
        String productName,
        Long sellerId,
        String sellerName,
        BigDecimal price,
        int stockQuantity,
        ProductCondition condition,
        boolean active
) {}
