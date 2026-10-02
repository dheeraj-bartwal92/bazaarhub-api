package com.bazaarhub.dto.product;

public record ProductResponse(
        Long id,
        String name,
        String description,
        String brandName,
        String sku,
        String imageUrl,
        boolean active,
        String categoryName
) {}
