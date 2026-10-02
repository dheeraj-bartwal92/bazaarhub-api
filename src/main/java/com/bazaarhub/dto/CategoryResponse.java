package com.bazaarhub.dto;

public record CategoryResponse(
        Long id,
        String name,
        String slug,
        String description,
        String imageUrl,
        boolean active,
        Long parentId,
        String parentName
) {}