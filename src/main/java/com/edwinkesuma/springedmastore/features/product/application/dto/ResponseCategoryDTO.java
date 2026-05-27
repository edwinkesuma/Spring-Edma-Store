package com.edwinkesuma.springedmastore.features.product.application.dto;

import com.edwinkesuma.springedmastore.features.product.domain.enums.CategoryType;

import java.util.UUID;

public record ResponseCategoryDTO(
        UUID id,
        String name,
        String slug,
        String iconUrl,
        CategoryType type,
        Integer sortOrder,
        boolean isActive
) {
}
