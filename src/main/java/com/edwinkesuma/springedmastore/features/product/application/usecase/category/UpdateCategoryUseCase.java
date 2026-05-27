package com.edwinkesuma.springedmastore.features.product.application.usecase.category;

import com.edwinkesuma.springedmastore.features.product.application.dto.RequestUpdateCategoryDTO;
import com.edwinkesuma.springedmastore.features.product.application.dto.ResponseCategoryDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface UpdateCategoryUseCase {
    ResponseCategoryDTO execute(RequestUpdateCategoryDTO request,
                                UUID categoryId,
                                MultipartFile image);
}
