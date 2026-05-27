package com.edwinkesuma.springedmastore.features.product.application.usecase.category;

import com.edwinkesuma.springedmastore.features.product.application.dto.ResponseCategoryDTO;

import java.util.UUID;

public interface GetDetailCategoryUseCase {
    ResponseCategoryDTO execute(UUID id);
}
