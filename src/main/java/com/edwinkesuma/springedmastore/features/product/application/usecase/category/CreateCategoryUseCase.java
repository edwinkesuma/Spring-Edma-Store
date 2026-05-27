package com.edwinkesuma.springedmastore.features.product.application.usecase.category;

import com.edwinkesuma.springedmastore.features.product.application.dto.RequestCreateCategoryDTO;
import com.edwinkesuma.springedmastore.features.product.application.dto.ResponseCategoryDTO;
import org.springframework.web.multipart.MultipartFile;

public interface CreateCategoryUseCase {
    ResponseCategoryDTO execute(RequestCreateCategoryDTO request, MultipartFile image);
}
