package com.edwinkesuma.springedmastore.features.product.application.usecase.category;

import com.edwinkesuma.springedmastore.features.product.application.dto.ResponseCategoryDTO;

import java.util.List;

public interface GetAllCategoryUseCase {
    List<ResponseCategoryDTO> execute();
}
