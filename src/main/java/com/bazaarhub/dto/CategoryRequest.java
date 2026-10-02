package com.bazaarhub.dto;

import jakarta.validation.constraints.NotBlank;

public record CategoryRequest(
        @NotBlank String name,
        String description,
        String imageUrl,
        Long parentId
) {
}
