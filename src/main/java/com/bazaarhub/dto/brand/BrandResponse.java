package com.bazaarhub.dto.brand;

public record BrandResponse(
        Long id,
        String name,
        String logoUrl,
        String description
) {}