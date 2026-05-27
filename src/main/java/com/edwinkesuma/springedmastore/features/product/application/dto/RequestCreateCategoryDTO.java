package com.edwinkesuma.springedmastore.features.product.application.dto;

import com.edwinkesuma.springedmastore.features.product.domain.enums.CategoryType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RequestCreateCategoryDTO(

        @NotBlank
        @Size(max = 100)
        String name,

        @NotNull
        CategoryType type,

        @NotNull
        @Min(0)
        Integer sortOrder,

        @NotNull
        Boolean isActive
) {
}
